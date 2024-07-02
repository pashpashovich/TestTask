package main.java.ru.clevertec.check.service;

import main.java.ru.clevertec.check.models.CartItem;
import main.java.ru.clevertec.check.models.DiscountCard;
import main.java.ru.clevertec.check.models.Product;
import main.java.ru.clevertec.check.models.Receipt;

public class DiscountPolicy {
    public double calculateDiscount(Receipt receipt, DiscountCard discountCard) {
        double discount = 0;
        for (CartItem item : receipt.getItems()) {
            Product product = item.getProduct();
            if (product.isWholesale() && item.getQuantity() >= 5) {
                discount += product.getPrice() * item.getQuantity() * 0.10;
            } else {
                discount += product.getPrice() * item.getQuantity() * discountCard.getDiscountRate();
            }
        }
        return discount;
    }
}
