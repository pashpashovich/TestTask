package ru.clevertec.check;



import ru.clevertec.check.exceptions.*;
import ru.clevertec.check.models.CartItem;
import ru.clevertec.check.models.DiscountCard;
import ru.clevertec.check.models.Product;
import ru.clevertec.check.models.Receipt;
import ru.clevertec.check.repositories.DBUtils;
import ru.clevertec.check.repositories.DiscountCardRepository;
import ru.clevertec.check.repositories.ProductRepository;
import ru.clevertec.check.service.DiscountPolicy;
import ru.clevertec.check.service.ReceiptPrinter;

import java.io.IOException;
import java.util.*;

public class CheckRunner {
    public static void main(String[] args) throws Exception {
        String results ="result.csv";
        String saveToFile=null;
        try {
            Map<String, CartItem> cartItems = new HashMap<>();
            Map<Integer,Integer> products=new HashMap<>();
            String discountCardNumber = null;
            String url=null, username=null,password=null;
            double balanceDebitCard = 0.0;
            for (String arg : args) {
                if (arg.startsWith("discountCard=")) {
                    discountCardNumber = arg.split("=")[1];
                } else if (arg.startsWith("balanceDebitCard=")) {
                    balanceDebitCard = Double.parseDouble(arg.split("=")[1]);
                } else if(arg.startsWith("saveToFile=")) {
                    saveToFile=arg.split("=")[1];
                } else if(arg.startsWith("datasource.url=")) {
                    url = arg.split("=")[1];
                } else if(arg.startsWith("datasource.username=")) {
                username = arg.split("=")[1];
            } else if(arg.startsWith("datasource.password=")) {
                    password= arg.split("=")[1];
                }
                else {
                    String[] parts = arg.split("-");
                    products.merge(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer::sum);
                }
            }
            DBUtils dbUtils= new DBUtils(url,username,password);
            ProductRepository productRepo = new ProductRepository(dbUtils);
            DiscountCardRepository discountCardRepo = new DiscountCardRepository(dbUtils);
            if (saveToFile==null) throw Objects.requireNonNull(ExceptionFactory.createException(ExceptionType.READFILEEXCEPTION, 0, null, 0, null,results));
            try {
                for (Map.Entry<Integer, Integer> entry : products.entrySet()) {
                    Integer productId = entry.getKey();
                    Integer quantity = entry.getValue();
                    Product product = productRepo.findById(productId);
                    if (product == null) {
                        throw Objects.requireNonNull(ExceptionFactory.createException(ExceptionType.NOPRODUCTEXCEPTION, productId, null, 0, null,saveToFile));
                    }
                    if (product.getQuantityInStock() < quantity) {
                        throw Objects.requireNonNull(ExceptionFactory.createException(ExceptionType.INSUFFICIENTSTOCKEXCEPTION, 0, product, quantity, null,saveToFile));
                    }
                    cartItems.put(product.getName(), new CartItem(product, quantity));
                }
            } catch (NoProductException | InsufficientStockException e) {
                System.out.println(e);
                return;
            }
            if (cartItems.isEmpty()) throw Objects.requireNonNull(ExceptionFactory.createException(ExceptionType.INCORRECTREQUESTEXCEPTION, 0, null, 0, null,saveToFile));
            Optional<DiscountCard> discountCard = Optional.empty();
            if (discountCardNumber == null) {
                System.out.println("Скидочная карта не была представлена");
            } else {
                discountCard = discountCardRepo.findById(discountCardNumber);
                if (discountCard.isEmpty()) {
                    try {
                        throw Objects.requireNonNull(ExceptionFactory.createException(ExceptionType.NODISCOUNTCARDEXCEPTION, 0, null, 0, discountCardNumber,saveToFile));
                    } catch (NoDiscountCardException e) {
                        System.out.println(e);
                    }
                }
            }
            List<CartItem> cartItemsList = new ArrayList<>(cartItems.values());
            double total = cartItemsList.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum();
            DiscountPolicy discountPolicy = new DiscountPolicy();
            double discount = discountPolicy.calculateDiscount(new Receipt.ReceiptBuilder().setItems(cartItemsList).setTotal(total).build(), discountCard);
            double finalTotal = total - discount;
            if (finalTotal>balanceDebitCard) throw Objects.requireNonNull(ExceptionFactory.createException(ExceptionType.NOTENOUGHMONEYEXCEPTION, 0, null, 0, null,saveToFile));
            Receipt receipt = new Receipt.ReceiptBuilder()
                    .setItems(cartItemsList)
                    .setTotal(total)
                    .setDiscount(discount)
                    .setFinalTotal(finalTotal)
                    .setDiscountCard(discountCard)
                    .build();

            ReceiptPrinter printer = new ReceiptPrinter();
            printer.printToConsole(receipt);
            printer.printToFile(receipt, saveToFile);
        }
        catch (ReadFileException | IncorrectRequestException | NotEnoughMoneyException | IOException e) {
            System.out.println(e);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка в чтении значений: " + e.getMessage());
        }
    }
}
