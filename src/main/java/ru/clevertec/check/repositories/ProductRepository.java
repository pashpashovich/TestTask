package ru.clevertec.check.repositories;


import ru.clevertec.check.models.Product;
import ru.clevertec.check.models.ProductImpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductRepository {

    private static final String GET_PRODUCT_BY_ID = "SELECT * FROM \"public\".\"product\" WHERE id=?";
    private static final String INSERT_PRODUCT = "INSERT INTO \"public\".\"product\" (description, price, quantity_in_stock, wholesale_product) VALUES (?, ?, ?, ?) RETURNING id";
    private static final String UPDATE_PRODUCT = "UPDATE \"public\".\"product\" SET description = ?, price = ?, quantity_in_stock = ?, wholesale_product = ? WHERE id = ?";
    private static final String REDUCE_PRODUCT_QUANTITY = "UPDATE \"public\".\"product\" SET quantity_in_stock = quantity_in_stock - ? WHERE id = ? AND quantity_in_stock >= ?";

    private static final String toDeleteProductByID = "DELETE FROM \"public\".\"product\" WHERE id = ?";


    private final DBUtils dbUtils;

    public ProductRepository(DBUtils dbUtils) {
        this.dbUtils = dbUtils;
    }

    public <Optional> Product findById(int id) {
        Product product = null;
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_PRODUCT_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                int quantity_in_stock = rs.getInt("quantity_in_stock");
                boolean wholesale_product = rs.getBoolean("wholesale_product");
                product = new ProductImpl(id, description, price, quantity_in_stock, wholesale_product);
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return product;
    }

    public void save(Product product) {
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PRODUCT)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setInt(3, product.getQuantityInStock());
            preparedStatement.setBoolean(4, product.isWholesale());

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                if (product instanceof ProductImpl) {
                    ((ProductImpl) product).setId(generatedId);
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }

    public void update(Product product) {
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PRODUCT)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setInt(3, product.getQuantityInStock());
            preparedStatement.setBoolean(4, product.isWholesale());
            preparedStatement.setInt(5, product.getId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating product failed, no rows affected.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }

    public boolean deleteById(int id) {
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(toDeleteProductByID)) {
            statement.setInt(1, id);
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            return false;
        }
    }

    public void reduceQuantity(Map<Integer, Integer> products) throws SQLException {
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(REDUCE_PRODUCT_QUANTITY)) {
            connection.setAutoCommit(false);
            try {
                for (Map.Entry<Integer, Integer> entry : products.entrySet()) {
                    int productId = entry.getKey();
                    int quantity = entry.getValue();
                    preparedStatement.setInt(1, quantity);
                    preparedStatement.setInt(2, productId);
                    preparedStatement.setInt(3, quantity);
                    int affectedRows = preparedStatement.executeUpdate();
                    if (affectedRows == 0) {
                        throw new SQLException("Недостаточно товаров на складе для продукта с id " + productId);
                    }
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }
}


