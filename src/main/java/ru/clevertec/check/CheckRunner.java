package main.java.ru.clevertec.check;
import main.java.ru.clevertec.check.models.CartItem;
import main.java.ru.clevertec.check.models.DiscountCard;
import main.java.ru.clevertec.check.models.Product;
import main.java.ru.clevertec.check.models.Receipt;
import main.java.ru.clevertec.check.repositories.DiscountCardRepository;
import main.java.ru.clevertec.check.repositories.ProductRepository;
import main.java.ru.clevertec.check.service.DiscountPolicy;
import main.java.ru.clevertec.check.service.ReceiptPrinter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CheckRunner {
    public static void main(String[] args) throws IOException {


        if (args.length < 2) {
            System.err.println("Usage: java CheckRunner id-quantity discountCard=xxxx balanceDebitCard=xxxx");
            return;
        }

        String productFilePath = "./src/main/resources/products.csv";
        String discountCardFilePath = "./src/main/resources/discountCards.csv";

        try {
            ProductRepository productRepo = new ProductRepository(productFilePath);
            DiscountCardRepository discountCardRepo = new DiscountCardRepository(discountCardFilePath);

            List<CartItem> cartItems = new ArrayList<>();
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
                    Product product = productRepo.findById(productId);
                    if (product == null) {
                        System.err.println("Product with id " + productId + " not found.");
                        return;
                    }
                    cartItems.add(new CartItem(product, quantity));
                }
            }

            DiscountCard discountCard = discountCardRepo.findById(discountCardNumber);
            if (discountCard == null) {
                System.err.println("Discount card not found.");
                return;
            }

            double total = cartItems.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum();
            DiscountPolicy discountPolicy = new DiscountPolicy();
            double discount = discountPolicy.calculateDiscount(new Receipt.Builder().setItems(cartItems).setTotal(total).build(), discountCard);
            double finalTotal = total - discount;

            Receipt receipt = new Receipt.Builder()
                    .setItems(cartItems)
                    .setTotal(total)
                    .setDiscount(discount)
                    .setFinalTotal(finalTotal)
                    .build();

            ReceiptPrinter printer = new ReceiptPrinter();
            printer.printToConsole(receipt);
            printer.printToFile(receipt, "result.csv");

        } catch (IOException e) {
            System.err.println("Error reading files: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing input: " + e.getMessage());
        }
    }
}
