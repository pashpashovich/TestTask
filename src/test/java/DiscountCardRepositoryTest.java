
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.clevertec.check.models.DiscountCard;
import ru.clevertec.check.repositories.DBUtils;
import ru.clevertec.check.repositories.DiscountCardRepository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DiscountCardRepositoryTest {

    private DBUtils dbUtils;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private DiscountCardRepository discountCardRepository;

    @BeforeEach
    void setUp() throws SQLException, IOException {
        dbUtils = mock(DBUtils.class);
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        when(dbUtils.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        discountCardRepository = new DiscountCardRepository(dbUtils);
    }
    @Test
    void testFindByIdNotFound() throws SQLException {
        String cardNumber = "456";
        when(resultSet.next()).thenReturn(false);
        Optional<DiscountCard> discountCard = discountCardRepository.findById(cardNumber);
        assertFalse(discountCard.isPresent());
    }
}