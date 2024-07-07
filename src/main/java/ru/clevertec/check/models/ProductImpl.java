package ru.clevertec.check.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductImpl implements Product {
    private int id;
    private final String name;
    private final double price;
    private final int quantityInStock;
    private final boolean isWholesale;


    public ProductImpl(int id, String name, double price, int quantityInStock, boolean isWholesale) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.isWholesale = isWholesale;
    }

    @JsonCreator
    public ProductImpl(@JsonProperty("description") String name,
                       @JsonProperty("price") double price,
                       @JsonProperty("quantity") int quantityInStock,
                       @JsonProperty("isWholesale") boolean isWholesale) {
        this.name = name;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.isWholesale = isWholesale;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public int getQuantityInStock() {
        return quantityInStock;
    }

    @Override
    public boolean isWholesale() {
        return isWholesale;
    }


}
