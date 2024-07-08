package ru.clevertec.check.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DiscountCardImpl implements DiscountCard {

    @JsonProperty("discountCard")
    private final int number;
    @JsonProperty("discountAmount")
    private final int discountRate;


    @JsonCreator
    public DiscountCardImpl(@JsonProperty("discountCard") int discountCard,  @JsonProperty("discountAmount") int discountAmount) {
        this.number = discountCard;
        this.discountRate = discountAmount;
    }

    @Override
    public int getNumber() { return number; }
    @Override
    public int getDiscountRate() { return discountRate; }
}
