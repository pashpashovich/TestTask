import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.clevertec.check.models.CartItem;
import ru.clevertec.check.models.DiscountCard;
import ru.clevertec.check.models.Product;
import ru.clevertec.check.models.Receipt;
import ru.clevertec.check.service.ReceiptPrinter;

import java.io.*;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class ReceiptPrinterTest {

    private ReceiptPrinter receiptPrinter;

    @Mock
    private Receipt mockReceipt;
    @Mock
    private CartItem mockCartItem;
    @Mock
    private Product mockProduct;
    @Mock
    private DiscountCard mockDiscountCard;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        receiptPrinter = new ReceiptPrinter();
        when(mockProduct.getName()).thenReturn("Test Product");
        when(mockProduct.getPrice()).thenReturn(10.0);
        when(mockProduct.isWholesale()).thenReturn(true);
        when(mockCartItem.getProduct()).thenReturn(mockProduct);
        when(mockCartItem.getQuantity()).thenReturn(5);
        when(mockDiscountCard.getNumber()).thenReturn("1234");
        when(mockDiscountCard.getDiscountRate()).thenReturn(0.05);
        when(mockReceipt.getItems()).thenReturn(Arrays.asList(mockCartItem));
        when(mockReceipt.getDiscountCard()).thenReturn(Optional.of(mockDiscountCard));
        when(mockReceipt.getTotal()).thenReturn(50.0);
        when(mockReceipt.getDiscount()).thenReturn(2.5);
        when(mockReceipt.getFinalTotal()).thenReturn(47.5);
    }

    @Test
    void testPrintToFile() throws IOException {
        String filePath = "test_receipt.csv";
        receiptPrinter.printToFile(mockReceipt, filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            assertEquals("Date;Time", reader.readLine());
        }
        new File(filePath).delete();
    }

    @Test
    void testPrintToConsole() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        receiptPrinter.printToConsole(mockReceipt);

        String output = outContent.toString();
        assertTrue(output.contains("Чек:"));
        assertTrue(output.contains("Дата:"));
        assertTrue(output.contains("Время:"));
        assertTrue(output.contains("Test Product"));
        assertTrue(output.contains("1234"));
        assertTrue(output.contains("Сумма: 50,0"));
        assertTrue(output.contains("Скидка: 2,5"));
        assertTrue(output.contains("Итого к оплате: 47,5"));

        System.setOut(System.out);
    }
}
