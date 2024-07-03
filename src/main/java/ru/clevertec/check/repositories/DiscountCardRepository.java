package main.java.ru.clevertec.check.repositories;

import main.java.ru.clevertec.check.models.DiscountCard;
import main.java.ru.clevertec.check.models.DiscountCardImpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DiscountCardRepository implements Repository<DiscountCard> {
    private final Map<String, DiscountCard> discountCards;

    public DiscountCardRepository(String filePath) throws IOException {
        this.discountCards = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String id = values[0];
                String number = values[1];
                double discountRate = Double.parseDouble(values[2]);
                this.discountCards.put(id, new DiscountCardImpl(number, discountRate));
            }
        }
    }

    @Override
    public Optional <DiscountCard> findById(String number) {
        for (DiscountCard card:discountCards.values()) {
            if (Objects.equals(card.getNumber(), number)) {
                return Optional.of(card);
            }
        }
        return Optional.empty();
    }
}
