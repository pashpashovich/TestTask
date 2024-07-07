import org.junit.jupiter.api.Test;
import ru.clevertec.check.models.*;
import ru.clevertec.check.repositories.DBUtils;
import ru.clevertec.check.repositories.DiscountCardRepository;
import ru.clevertec.check.repositories.ProductRepository;
import ru.clevertec.check.service.DiscountPolicy;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DiscountPolicyTest {

    @Test
    public void testCalculateDiscount_noDiscounts() {
        DiscountPolicy discountPolicy = new DiscountPolicy();
        Receipt receipt = new Receipt.ReceiptBuilder().setItems(List.of(new CartItem(new ProductImpl(1,"Chips",2,22,true),3),new CartItem(new ProductImpl(2,"Halva",3,4,false),3))).build();
        double discount = discountPolicy.calculateDiscount(receipt, Optional.empty());
        assertEquals(0, discount, 0.01);
    }

    @Test
    public void testCalculateDiscount_withDiscountCard() throws IOException {
        DiscountPolicy discountPolicy = new DiscountPolicy();
        Optional<DiscountCard> discountCard = Optional.of(new DiscountCardImpl("1111", 3));
        Receipt receipt = new Receipt.ReceiptBuilder().setItems(List.of(new CartItem(new ProductImpl(1,"Chips",2,22,true),3),new CartItem(new ProductImpl(2,"Halva",3,4,false),3))).build();
        double discount = discountPolicy.calculateDiscount(receipt, discountCard);
        assertEquals(0.45, discount, 0.01);
    }

    @Test
    public void testCalculateDiscount_wholesaleDiscount() {
        DiscountPolicy discountPolicy = new DiscountPolicy();
        Receipt receipt = new Receipt.ReceiptBuilder().setItems(List.of(new CartItem(new ProductImpl(1,"Chips",2,22,true),5),new CartItem(new ProductImpl(2,"Halva",3,4,false),3))).build();
        double discount = discountPolicy.calculateDiscount(receipt, Optional.empty());
        assertEquals(1, discount, 0.01);
    }

    @Test
    public void testCalculateDiscount_wholesaleAndCardDiscount() {
        DiscountPolicy discountPolicy = new DiscountPolicy();
        Optional<DiscountCard> discountCard = Optional.of(new DiscountCardImpl("1111", 3));
        Receipt receipt = new Receipt.ReceiptBuilder().setItems(List.of(new CartItem(new ProductImpl(1,"Chips",2,22,true),5),new CartItem(new ProductImpl(2,"Halva",3,4,false),3))).build();
        double discount = discountPolicy.calculateDiscount(receipt, discountCard);
        assertEquals(1.27, discount, 0.01);
    }
    @Test
    public void testCalculateDiscount_emptyReceipt() {
        Receipt receipt = new Receipt.ReceiptBuilder().setItems(null).build();
        DiscountPolicy discountPolicy = new DiscountPolicy();
        double discount = discountPolicy.calculateDiscount(receipt, Optional.empty());
        assertEquals(0, discount, 0.01);
    }
}
