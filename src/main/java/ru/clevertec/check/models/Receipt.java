package main.java.ru.clevertec.check.models;

import java.util.List;
import java.util.Optional;

public class Receipt {
    private final List<CartItem> items;
    private final double total;
    private final double discount;
    private final double finalTotal;
    private final Optional<DiscountCard> discountCard;

    private Receipt(ReceiptBuilder builder) {
        this.items = builder.items;
        this.total = builder.total;
        this.discount = builder.discount;
        this.finalTotal = builder.finalTotal;
        this.discountCard=builder.discountCard;
    }

    public List<CartItem> getItems() { return items; }
    public double getTotal() { return total; }
    public double getDiscount() { return discount; }
    public double getFinalTotal() { return finalTotal; }

    public Optional<DiscountCard> getDiscountCard() {
        return discountCard;
    }

    public static class ReceiptBuilder {
        private List<CartItem> items;
        private double total;
        private double discount;
        private double finalTotal;
        private Optional<DiscountCard> discountCard;


        public ReceiptBuilder setItems(List<CartItem> items) {
            this.items = items;
            return this;
        }

        public ReceiptBuilder setTotal(double total) {
            this.total = total;
            return this;
        }

        public ReceiptBuilder setDiscount(double discount) {
            this.discount = discount;
            return this;
        }

        public ReceiptBuilder setFinalTotal(double finalTotal) {
            this.finalTotal = finalTotal;
            return this;
        }

        public ReceiptBuilder setDiscountCard(Optional<DiscountCard> discountCard) {
            this.discountCard = discountCard;
            return this;
        }

        public Receipt build() {
            return new Receipt(this);
        }
    }
}
