package main.java.ru.clevertec.check.repositories;

import main.java.ru.clevertec.check.models.Product;
import main.java.ru.clevertec.check.models.ProductImpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProductRepository {
    private Map<Integer, Product> products = new HashMap<>();

    public ProductRepository(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (stripQuotes(values[2]).endsWith("l")) {
                    values[1] = values[1] + "," + values[2];
                    values[2]=values[3];
                    values[3]=values[4];
                    values[5]=values[6];
                }
                int id = Integer.parseInt(stripQuotes(values[0]));
                String name = stripQuotes(values[1]);
                double price = parsePrice(stripQuotes(values[2]),stripQuotes(values[3]));
                boolean isWholesale = "+".equals(stripQuotes(values[5]));
                products.put(id, new ProductImpl(id, name, price, isWholesale));
            }
        }
        System.out.println(products.values());
    }

    private String stripQuotes(String value) {
        return value.replaceAll("^\"|\"$", "");
    }

    private double parsePrice(String value1,String value2) throws IOException {
        try {
            return Double.parseDouble(value1+"."+value2);
        } catch (NumberFormatException e) {
            throw new IOException("Failed to parse price value: " + value1+value2, e);
        }
    }

    public <Optional> Product findById(int id) {
        for (Product product: products.values()) {
            if (product.getId()==id) {
                return product;
            }

        }
        return null;

    }


}


