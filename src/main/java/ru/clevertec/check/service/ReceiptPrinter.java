package main.java.ru.clevertec.check.service;

import main.java.ru.clevertec.check.models.CartItem;
import main.java.ru.clevertec.check.models.Product;
import main.java.ru.clevertec.check.models.Receipt;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ReceiptPrinter {
    public void printToConsole(Receipt receipt) {
        System.out.println("Receipt:");
        printReceipt(receipt);
    }

    public void printToFile(Receipt receipt, String filePath) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            pw.println("Date,Time");
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalTime localTime=LocalTime.now();
            DateTimeFormatter localTimeFormatter =DateTimeFormatter.ofPattern("HH:mm:ss");
            pw.println(currentDate.format(formatter)+","+localTime.format(localTimeFormatter));
            pw.println();
            pw.println("QTY, DESCRIPTION,PRICE,DISCOUNT,TOTAL");
            double discount,total;
            for (CartItem item: receipt.getItems()) {
                if (item.getQuantity()>=5) discount = item.getProduct().getPrice() * 0.1;
                else discount = item.getProduct().getPrice()* receipt.getDiscountCard().get().getDiscountRate();
                pw.println(item.getQuantity()+","+item.getProduct().getName()+","+String.format("%.2f$",item.getProduct().getPrice()).replace(",", ".")+","+String.format("%.2f$",discount).replace(",", ".")+","+String.format("%.2f$",(item.getQuantity()*item.getProduct().getPrice())).replace(",", "."));
            }
            if (receipt.getDiscountCard().isPresent()) {
                pw.println();
                pw.println("DISCOUNT CARD, DISCOUNT PERCENTAGE");
                pw.println(receipt.getDiscountCard().get().getNumber()+","+ (int) (receipt.getDiscountCard().get().getDiscountRate() * 100) +"%");
            }
            pw.println();
            pw.println("TOTAL PRICE, TOTAL DISCOUNT, TOTAL WITH DISCOUNT");
            pw.println(String.format("%.2f$",receipt.getTotal()).replace(",",".")+","+String.format("%.2f$",receipt.getDiscount()).replace(",",".")+","+String.format("%.2f$",receipt.getFinalTotal()).replace(",","."));
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
