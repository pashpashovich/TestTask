package main.java.ru.clevertec.check.service;

import main.java.ru.clevertec.check.models.CartItem;
import main.java.ru.clevertec.check.models.DiscountCard;
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
        System.out.println("Чек:");
        printReceipt(receipt);
    }

    public void printToFile(Receipt receipt, String filePath) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            pw.println("Date;Time");
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalTime localTime=LocalTime.now();
            DateTimeFormatter localTimeFormatter =DateTimeFormatter.ofPattern("HH:mm:ss");
            pw.println(currentDate.format(formatter)+";"+localTime.format(localTimeFormatter));
            pw.println();
            pw.println("QTY;DESCRIPTION;PRICE;DISCOUNT;TOTAL");
            double discount = 0,total;
            for (CartItem item: receipt.getItems()) {
                if (item.getQuantity()>=5 && item.getProduct().isWholesale()) discount = item.getProduct().getPrice()*item.getQuantity() * 0.1;
                else if (receipt.getDiscountCard().isPresent()) discount = item.getProduct().getPrice()*item.getQuantity()* receipt.getDiscountCard().get().getDiscountRate();
                pw.println(item.getQuantity()+";"+item.getProduct().getName()+";"+String.format("%.2f$",item.getProduct().getPrice()).replace(",", ".")+";"+String.format("%.2f$",discount).replace(",", ".")+";"+String.format("%.2f$",(item.getQuantity()*item.getProduct().getPrice())).replace(",", "."));
            }
            if (receipt.getDiscountCard().isPresent()) {
                pw.println();
                pw.println("DISCOUNT CARD;DISCOUNT PERCENTAGE");
                pw.println(receipt.getDiscountCard().get().getNumber()+";"+ (int) (receipt.getDiscountCard().get().getDiscountRate() * 100) +"%");
            }
            pw.println();
            pw.println("TOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT");
            pw.println(String.format("%.2f$",receipt.getTotal()).replace(",",".")+";"+String.format("%.2f$",receipt.getDiscount()).replace(",",".")+";"+String.format("%.2f$",receipt.getFinalTotal()).replace(",","."));
        }
    }

    private void printReceipt(Receipt receipt) {
        System.out.println("Дата: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        System.out.println("Время: " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        System.out.println();

        System.out.printf("%-8s%-20s%-10s%-10s%-10s%n", "Кол-во", "Описание", "Цена", "Скидка", "Итого");
        for (CartItem item : receipt.getItems()) {
            Product product = item.getProduct();
            double discount = (item.getQuantity() >= 5 && item.getProduct().isWholesale()) ? product.getPrice()*item.getQuantity() * 0.1 : product.getPrice()*item.getQuantity() * receipt.getDiscountCard().orElse(new DiscountCard() {
                @Override
                public String getNumber() {
                    return null;
                }

                @Override
                public double getDiscountRate() {
                    return 0;
                }
            }).getDiscountRate();
            double total = item.getQuantity() * product.getPrice();

            System.out.printf("%-8d%-20s%-10.2f%-10.2f%-10.2f%n",
                    item.getQuantity(),
                    product.getName(),
                    product.getPrice(),
                    discount,
                    total);
        }

        if (receipt.getDiscountCard().isPresent()) {
            System.out.println();
            System.out.printf("%-20s%-10s%n", "Дисконтная карта", "Процент по скидке");
            System.out.printf("%-20s%-10d%n", receipt.getDiscountCard().get().getNumber(), (int) (receipt.getDiscountCard().get().getDiscountRate() * 100));
        }

        System.out.println();
        System.out.printf("Сумма: %.2f$%n", receipt.getTotal());
        System.out.printf("Скидка: %.2f$%n", receipt.getDiscount());
        System.out.printf("Итого к оплате: %.2f$%n", receipt.getFinalTotal());
}
}
