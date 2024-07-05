package ru.clevertec.check.models;

public class ProductImpl implements Product {
    private final int id;
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
