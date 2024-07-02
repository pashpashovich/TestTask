package main.java.ru.clevertec.check.models;

import main.java.ru.clevertec.check.models.Product;

public class ProductImpl implements Product {
    private final int id;
    private final String name;
    private final double price;
    private final boolean isWholesale;

    public ProductImpl(int id, String name, double price, boolean isWholesale) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.isWholesale = isWholesale;
    }

    @Override
    public int getId() { return id; }
    @Override
    public String getName() { return name; }
    @Override
    public double getPrice() { return price; }
    @Override
    public boolean isWholesale() { return isWholesale; }
}
