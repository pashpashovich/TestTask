package ru.clevertec.check.service;

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
    public Receipt generateReceipt(Map<Integer, Integer> products, String discountCardNumber, double balanceDebitCard, DBUtils dbUtils) throws Exception {
        Map<String, CartItem> cartItems = new HashMap<>();
        ProductRepository productRepo = new ProductRepository(dbUtils);
        DiscountCardRepository discountCardRepo = new DiscountCardRepository(dbUtils);
            for (Map.Entry<Integer, Integer> entry : products.entrySet()) {
                Integer productId = entry.getKey();
                Integer quantity = entry.getValue();
                Product product = productRepo.findById(productId);
                if (product == null) {
                    throw Objects.requireNonNull(ExceptionFactory.createException(ExceptionType.NOPRODUCTEXCEPTION, productId, null, 0, null, null));
                }
                if (product.getQuantityInStock() < quantity) {
                    throw Objects.requireNonNull(ExceptionFactory.createException(ExceptionType.INSUFFICIENTSTOCKEXCEPTION, 0, product, quantity, null, null));
                }
                cartItems.put(product.getName(), new CartItem(product, quantity));
            }
        if (cartItems.isEmpty()) {
            throw Objects.requireNonNull(ExceptionFactory.createException(ExceptionType.INCORRECTREQUESTEXCEPTION, 0, null, 0, null, null));
        }
        Optional<DiscountCard> discountCard = Optional.empty();
        if (discountCardNumber != null) {
            discountCard = discountCardRepo.findByNumber(Integer.parseInt(discountCardNumber));
            if (discountCard.isEmpty()) {
                throw Objects.requireNonNull(ExceptionFactory.createException(ExceptionType.NODISCOUNTCARDEXCEPTION, 0, null, 0, discountCardNumber, null));
            }
        }
        List<CartItem> cartItemsList = new ArrayList<>(cartItems.values());
        double total = cartItemsList.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum();
        DiscountPolicy discountPolicy = new DiscountPolicy();
        double discount = discountPolicy.calculateDiscount(new Receipt.ReceiptBuilder().setItems(cartItemsList).setTotal(total).build(), discountCard);
        double finalTotal = total - discount;
        if (finalTotal > balanceDebitCard) {
            throw Objects.requireNonNull(ExceptionFactory.createException(ExceptionType.NOTENOUGHMONEYEXCEPTION, 0, null, 0, null, null));
        }
        return new Receipt.ReceiptBuilder()
                .setItems(cartItemsList)
                .setTotal(total)
                .setDiscount(discount)
                .setFinalTotal(finalTotal)
                .setDiscountCard(discountCard)
                .build();
    }
}

