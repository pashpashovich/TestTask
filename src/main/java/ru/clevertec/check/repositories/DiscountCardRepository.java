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
    private static final String toGetCardById="SELECT * FROM \"public\".\"discount_card\" WHERE id=?";
    private static final String toSaveCard = "INSERT INTO \"public\".\"discount_card\" (number, amount) VALUES (?, ?)";
    private static final String toUpdateCard = "UPDATE \"public\".\"discount_card\" SET number = ?, amount = ? WHERE id = ?";
    private static final String toDeleteCardByNumber = "DELETE FROM \"public\".\"discount_card\" WHERE id = ?";

    private final Map<String, DiscountCard> discountCards= new HashMap<>();
    private final DBUtils dbUtils;

    public DiscountCardRepository(DBUtils dbUtils) throws IOException {
        this.dbUtils=dbUtils;
    }

    public Optional<DiscountCard> findByNumber(int number) {
        DiscountCard discountCard=null;
        try(Connection connection=dbUtils.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(toGetCardByNumber)) {
            preparedStatement.setInt(1, number);
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

    public void save(DiscountCard discountCard) {
        if (findByNumber(discountCard.getNumber()).isPresent()) {
            throw new IllegalArgumentException("Скидочная карта с номером " + discountCard.getNumber() + " уже существует");
        }
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(toSaveCard)) {
            statement.setInt(1, discountCard.getNumber());
            statement.setInt(2, discountCard.getDiscountRate());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }

    public void update(DiscountCard discountCard,int id) throws IOException {
        if (findByNumber(discountCard.getNumber()).isPresent()) {
            throw new IllegalArgumentException("Скидочная карта с номером " + discountCard.getNumber() + " уже существует");
        }
        if(findById(String.valueOf(id)).isEmpty()) {
            throw new IllegalArgumentException("Скидочная карта с ID " + id + " не существует");
        }
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(toUpdateCard)) {
            statement.setInt(1, discountCard.getNumber());
            statement.setInt(2, discountCard.getDiscountRate());
            statement.setInt(3, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }

    public boolean deleteById(int number) {
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(toDeleteCardByNumber)) {
            statement.setInt(1, number);
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<DiscountCard> findById(String id) throws IOException {
        DiscountCard discountCard=null;
        try(Connection connection=dbUtils.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(toGetCardById)) {
            preparedStatement.setInt(1, Integer.parseInt(id));
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next()) {
                int number = rs.getInt("number");
                int amount = rs.getInt("amount");
                discountCard=new DiscountCardImpl(number,amount);
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return Optional.ofNullable(discountCard);
    }
}
