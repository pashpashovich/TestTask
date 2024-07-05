package ru.clevertec.check.service;


import ru.clevertec.check.models.CartItem;
import ru.clevertec.check.models.DiscountCard;
import ru.clevertec.check.models.Product;
import ru.clevertec.check.models.Receipt;

import java.util.Optional;

public class DiscountPolicy {

    public double calculateDiscount(Receipt receipt, Optional<DiscountCard> discountCard) {
        double discount = 0;
        for (CartItem item : receipt.getItems()) {
            Product product = item.getProduct();
            if (product.isWholesale() && item.getQuantity() >= 5) {
                discount += product.getPrice() * item.getQuantity() * 0.10;
            } else {
                if (discountCard.isPresent()) discount += product.getPrice() * item.getQuantity() * discountCard.get().getDiscountRate();
            }
        }
        return discount;
    }
}
