package ru.clevertec.check.repositories;


import ru.clevertec.check.models.DiscountCard;
import ru.clevertec.check.models.DiscountCardImpl;
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
import java.util.Objects;
import java.util.Optional;

public class DiscountCardRepository implements Repository<DiscountCard> {
    private static final String toGetCardByNumber="SELECT * FROM \"public\".\"discount_card\" WHERE number=?";

    private final Map<String, DiscountCard> discountCards= new HashMap<>();
    private final DBUtils dbUtils;

    public DiscountCardRepository(DBUtils dbUtils) throws IOException {
        this.dbUtils=dbUtils;
    }

    @Override
    public Optional<DiscountCard> findById(String number) {
        DiscountCard discountCard=null;
        try(Connection connection=dbUtils.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(toGetCardByNumber)) {
            preparedStatement.setInt(1, Integer.parseInt(number));
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next()) {
                int amount = rs.getInt("amount");
                discountCard=new DiscountCardImpl(number,amount);
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return Optional.ofNullable(discountCard);
    }
}
