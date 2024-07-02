package main.java.ru.clevertec.check.repositories;

import main.java.ru.clevertec.check.models.DiscountCard;
import main.java.ru.clevertec.check.models.DiscountCardImpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DiscountCardRepository implements Repository<DiscountCard> {
    private final Map<String, DiscountCard> discountCards = new HashMap<>();

    public DiscountCardRepository(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String number = values[0];
                double discountRate = Double.parseDouble(values[1]);
                discountCards.put(number, new DiscountCardImpl(number, discountRate));
            }
        }
    }

    @Override
    public DiscountCard findById(String id) {
        return discountCards.get(id);
    }
}
