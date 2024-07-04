package main.java.ru.clevertec.check;

import main.java.ru.clevertec.check.exceptions.*;
import main.java.ru.clevertec.check.models.CartItem;
import main.java.ru.clevertec.check.models.DiscountCard;
import main.java.ru.clevertec.check.models.Product;
import main.java.ru.clevertec.check.models.Receipt;
import main.java.ru.clevertec.check.repositories.DiscountCardRepository;
import main.java.ru.clevertec.check.repositories.ProductRepository;
import main.java.ru.clevertec.check.service.DiscountPolicy;
import main.java.ru.clevertec.check.service.ReceiptPrinter;

import java.io.IOException;
import java.util.*;

public class CheckRunner {
    public static void main(String[] args) throws Exception {
        String productFilePath = "./src/main/resources/products.csv";
        String discountCardFilePath = "./src/main/resources/discountCards.csv";
        try {
            ProductRepository productRepo = new ProductRepository(productFilePath);
            DiscountCardRepository discountCardRepo = new DiscountCardRepository(discountCardFilePath);
            Map<String, CartItem> cartItems = new HashMap<>();
            String discountCardNumber = null;
            double balanceDebitCard = 0.0;

            for (String arg : args) {
                if (arg.startsWith("discountCard=")) {
                    discountCardNumber = arg.split("=")[1];
                } else if (arg.startsWith("balanceDebitCard=")) {
                    balanceDebitCard = Double.parseDouble(arg.split("=")[1]);
                } else {
                    String[] parts = arg.split("-");
                    int productId = Integer.parseInt(parts[0]);
                    int quantity = Integer.parseInt(parts[1]);

                    try {
                        Product product = productRepo.findById(productId);
                        if (product == null) {
                            throw Objects.requireNonNull(ExceptionFactory.createException(ExceptionType.NOPRODUCTEXCEPTION, productId, null, 0, null));
                        }
                        int existingQuantity = cartItems.getOrDefault(product.getName(), new CartItem(product, 0)).getQuantity();
                        if (product.getQuantityInStock() < existingQuantity + quantity) {
                            cartItems.put(product.getName(), new CartItem(product, product.getQuantityInStock()));
                            throw Objects.requireNonNull(ExceptionFactory.createException(ExceptionType.INSUFFICIENTSTOCKEXCEPTION, 0, product, existingQuantity + quantity, null));
                        }
                        cartItems.put(product.getName(), new CartItem(product, existingQuantity + quantity));
                    } catch (NoProductException | InsufficientStockException e) {
                        System.err.println(e);
                        return;
                    }
                }
            }
            if (cartItems.isEmpty()) throw Objects.requireNonNull(ExceptionFactory.createException(ExceptionType.INCORRECTREQUESTEXCEPTION, 0, null, 0, null));
            Optional<DiscountCard> discountCard = Optional.empty();
            if (discountCardNumber == null) {
                System.out.println("Скидочная карта не была представлена");
            } else {
                discountCard = discountCardRepo.findById(discountCardNumber);
                if (discountCard.isEmpty()) {
                    try {
                        throw Objects.requireNonNull(ExceptionFactory.createException(ExceptionType.NODISCOUNTCARDEXCEPTION, 0, null, 0, discountCardNumber));
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
            if (finalTotal>balanceDebitCard) throw Objects.requireNonNull(ExceptionFactory.createException(ExceptionType.NOTENOUGHMONEYEXCEPTION, 0, null, 0, null));
            Receipt receipt = new Receipt.ReceiptBuilder()
                    .setItems(cartItemsList)
                    .setTotal(total)
                    .setDiscount(discount)
                    .setFinalTotal(finalTotal)
                    .setDiscountCard(discountCard)
                    .build();

            ReceiptPrinter printer = new ReceiptPrinter();
            printer.printToConsole(receipt);
            printer.printToFile(receipt, "result.csv");
        }catch (IncorrectRequestException | NotEnoughMoneyException e) {
            System.out.println(e);
        }
        catch (IOException e) {
           throw Objects.requireNonNull(ExceptionFactory.createException(ExceptionType.READFILEEXCEPTION, 0, null, 0, null));
        } catch (NumberFormatException e) {
            System.err.println("Error parsing input: " + e.getMessage());
        }
    }
}
