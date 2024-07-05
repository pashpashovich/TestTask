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
import java.util.HashMap;
import java.util.Map;

public class ProductRepository {

    private static final String toGetProductByID="SELECT * FROM \"public\".\"product\" WHERE id=?";
    private final Map<Integer, Product> products = new HashMap<>();
    private final DBUtils dbUtils;

    public ProductRepository(DBUtils dbUtils) {
        this.dbUtils = dbUtils;
    }
    public <Optional> Product findById(int id) {
        Product product=null;
        try(Connection connection=dbUtils.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(toGetProductByID)) {
            preparedStatement.setInt(1,id);
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next()) {
                String description=rs.getString("description");
                double price=rs.getDouble("price");
                int quantity_in_stock=rs.getInt("quantity_in_stock");
                boolean wholesale_product=rs.getBoolean("wholesale_product");
                product=new ProductImpl(id,description,price,quantity_in_stock,wholesale_product);
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return product;
    }

}


