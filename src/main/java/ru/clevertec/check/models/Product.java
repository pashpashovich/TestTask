package ru.clevertec.check.models;

public interface Product {
    void setId(int id);
    int getId();
    String getName();
    double getPrice();
    int getQuantityInStock();
    boolean isWholesale();
}
