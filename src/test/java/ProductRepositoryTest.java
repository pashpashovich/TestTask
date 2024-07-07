
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.models.Product;
import ru.clevertec.check.repositories.DBUtils;
import ru.clevertec.check.repositories.ProductRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductRepositoryTest {

    private DBUtils dbUtils;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() throws SQLException {
        dbUtils = mock(DBUtils.class);
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        when(dbUtils.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        productRepository = new ProductRepository(dbUtils);
    }

    @Test
    void testFindById() throws SQLException {
        int productId = 123;
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("description")).thenReturn("Test Product");
        when(resultSet.getDouble("price")).thenReturn(19.99);
        when(resultSet.getInt("quantity_in_stock")).thenReturn(100);
        when(resultSet.getBoolean("wholesale_product")).thenReturn(true);
        Product product = productRepository.findById(productId);
        assertNotNull(product);
        assertEquals(productId, product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals(19.99, product.getPrice());
        assertEquals(100, product.getQuantityInStock());
        assertTrue(product.isWholesale());
    }

    @Test
    void testFindByIdNotFound() throws SQLException {
        int productId = 999;
        when(resultSet.next()).thenReturn(false);
        Product product = productRepository.findById(productId);
        assertNull(product);
    }
}
