package main.java.ru.clevertec.check.models;

public interface Product {
    int getId();
    String getName();
    double getPrice();
    int getQuantityInStock();
    boolean isWholesale();
}
