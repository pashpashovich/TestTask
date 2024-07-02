package main.java.ru.clevertec.check.models;

import java.util.List;

public class Receipt {
    private final List<CartItem> items;
    private final double total;
    private final double discount;
    private final double finalTotal;

    private Receipt(Builder builder) {
        this.items = builder.items;
        this.total = builder.total;
        this.discount = builder.discount;
        this.finalTotal = builder.finalTotal;
    }

    public List<CartItem> getItems() { return items; }
    public double getTotal() { return total; }
    public double getDiscount() { return discount; }
    public double getFinalTotal() { return finalTotal; }

    public static class Builder {
        private List<CartItem> items;
        private double total;
        private double discount;
        private double finalTotal;

        public Builder setItems(List<CartItem> items) {
            this.items = items;
            return this;
        }

        public Builder setTotal(double total) {
            this.total = total;
            return this;
        }

        public Builder setDiscount(double discount) {
            this.discount = discount;
            return this;
        }

        public Builder setFinalTotal(double finalTotal) {
            this.finalTotal = finalTotal;
            return this;
        }

        public Receipt build() {
            return new Receipt(this);
        }
    }
}
