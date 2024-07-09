package ru.clevertec.check.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.check.exceptions.*;
import ru.clevertec.check.models.CartItem;
import ru.clevertec.check.models.DiscountCard;
import ru.clevertec.check.models.Product;
import ru.clevertec.check.repositories.DiscountCardRepository;
import ru.clevertec.check.service.CheckRunner;
import ru.clevertec.check.models.Receipt;
import ru.clevertec.check.repositories.DBUtils;
import ru.clevertec.check.repositories.ProductRepository;
import ru.clevertec.check.service.DiscountPolicy;
import ru.clevertec.check.service.ReceiptPrinter;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

public class CheckServlet extends HttpServlet {

    private CheckRunner checkRunner;
    private DBUtils dbUtils;

    @Override
    public void init() throws ServletException {
        dbUtils = new DBUtils(System.getProperty("datasource.url"),
                System.getProperty("datasource.username"),
                System.getProperty("datasource.password"));
        checkRunner = new CheckRunner();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map requestMap = mapper.readValue(req.getInputStream(), Map.class);
            String discountCardNumber = requestMap.get("discountCard") != null ? requestMap.get("discountCard").toString() : null;
            double balanceDebitCard = Double.parseDouble(requestMap.get("balanceDebitCard").toString());
            Map<Integer, Integer> products = new HashMap<>();
            for (Map<String, Object> productMap : (Iterable<Map<String, Object>>) requestMap.get("products")) {
                Integer productId = Integer.parseInt(productMap.get("id").toString());
                Integer quantity = Integer.parseInt(productMap.get("quantity").toString());
                products.merge(productId, quantity, Integer::sum);
            }
            Map<String, CartItem> cartItems = new HashMap<>();
            ProductRepository productRepo = new ProductRepository(dbUtils);
            DiscountCardRepository discountCardRepo = new DiscountCardRepository(dbUtils);
            for (Map.Entry<Integer, Integer> entry : products.entrySet()) {
                Integer productId = entry.getKey();
                Integer quantity = entry.getValue();
                Product product = productRepo.findById(productId);
                if (product == null) {
                    throw new NoProductException("Продукт не найден","./result.csv");
                }
                if (product.getQuantityInStock() < quantity) {
                    throw new InsufficientStockException("Недостаточно товаров на складе","./result.csv");
                }
                cartItems.put(product.getName(), new CartItem(product, quantity));
            }
            if (cartItems.isEmpty()) {
                throw new IncorrectRequestException("Некорректный запрос","./result.csv");
            }
            Optional<DiscountCard> discountCard = Optional.empty();
            if (discountCardNumber != null) {
                discountCard = discountCardRepo.findByNumber(Integer.parseInt(discountCardNumber));
                if (discountCard.isEmpty()) {
                    throw new NoDiscountCardException("Скидочная карта не найдена");
                }
            }
            List<CartItem> cartItemsList = new ArrayList<>(cartItems.values());
            double total = cartItemsList.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum();
            DiscountPolicy discountPolicy = new DiscountPolicy();
            double discount = discountPolicy.calculateDiscount(new Receipt.ReceiptBuilder().setItems(cartItemsList).setTotal(total).build(), discountCard);
            double finalTotal = total - discount;
            if (finalTotal > balanceDebitCard) {
                System.out.println("Тут");
                throw new NotEnoughMoneyException("Недостаточно средств","./result.csv");
            }
            Receipt receipt = new Receipt.ReceiptBuilder()
                    .setItems(cartItemsList)
                    .setTotal(total)
                    .setDiscount(discount)
                    .setFinalTotal(finalTotal)
                    .setDiscountCard(discountCard)
                    .build();
            ProductRepository productRepository = new ProductRepository(dbUtils);
            productRepository.reduceQuantity(products);
            ReceiptPrinter.printToFile(receipt,"./result.csv");
            File downloadFile = new File("./result.csv");
            FileInputStream inStream = new FileInputStream(downloadFile);
            String mimeType = getServletContext().getMimeType("./result.csv");
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }
            resp.setContentType(mimeType);
            resp.setContentLength((int) downloadFile.length());
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=\"" + downloadFile.getName() + "\"";
            resp.setHeader(headerKey, headerValue);
            OutputStream outStream = resp.getOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            inStream.close();
            outStream.close();
        } catch (NoProductException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (InsufficientStockException | NotEnoughMoneyException | IncorrectRequestException |
                 NoDiscountCardException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка обработки запроса: " + e.getMessage());
        }
    }
}
