package main.java.ru.clevertec.check.service;

import main.java.ru.clevertec.check.models.CartItem;
import main.java.ru.clevertec.check.models.Product;
import main.java.ru.clevertec.check.models.Receipt;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ReceiptPrinter {
    public void printToConsole(Receipt receipt) {
        System.out.println("Receipt:");
        printReceipt(receipt);
    }

    public void printToFile(Receipt receipt, String filePath) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            pw.println("Receipt:");
            printReceipt(receipt, pw);
        }
    }

    private void printReceipt(Receipt receipt) {
        for (CartItem item : receipt.getItems()) {
            Product product = item.getProduct();
            System.out.printf("%s (x%d): %.2f%n", product.getName(), item.getQuantity(), product.getPrice());
        }
        System.out.printf("Total: %.2f%n", receipt.getTotal());
        System.out.printf("Discount: %.2f%n", receipt.getDiscount());
        System.out.printf("Final Total: %.2f%n", receipt.getFinalTotal());
    }

    private void printReceipt(Receipt receipt, PrintWriter pw) {
        for (CartItem item : receipt.getItems()) {
            Product product = item.getProduct();
            pw.printf("%s (x%d): %.2f%n", product.getName(), item.getQuantity(), product.getPrice());
        }
        pw.printf("Total: %.2f%n", receipt.getTotal());
        pw.printf("Discount: %.2f%n", receipt.getDiscount());
        pw.printf("Final Total: %.2f%n", receipt.getFinalTotal());
    }
}
