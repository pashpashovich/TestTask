package main.java.ru.clevertec.check.models;

import main.java.ru.clevertec.check.models.DiscountCard;

public class DiscountCardImpl implements DiscountCard {
    private final String number;
    private final double discountRate;

    public DiscountCardImpl(String number, double discountRate) {
        this.number = number;
        this.discountRate = discountRate/100;
    }

    @Override
    public String getNumber() { return number; }
    @Override
    public double getDiscountRate() { return discountRate; }
}
