package ru.fintech.qa.homework;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.fintech.qa.homework.utils.BeforeUtils;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestSQL {

    private static Connection connection;

    @BeforeEach
    public void init() throws SQLException {
        BeforeUtils.createData();
        connection = getNewConnection();
    }

    @AfterEach
    public void close() throws SQLException {
        connection.close();
    }

    private Connection getNewConnection() throws SQLException {
        String url = "jdbc:h2:mem:myDb";
        String user = "sa";
        String passwd = "sa";
        return DriverManager.getConnection(url, user, passwd);
    }

    @Test
    public void Test1() throws SQLException {
        String sql = "SELECT count(id) as count FROM public.animal;";
        PreparedStatement statement = connection.prepareStatement(sql);
        boolean hasResult = statement.execute();
        assertTrue(hasResult);
        ResultSet set = statement.getResultSet();
        set.next();
        int count = set.getInt("count");
        assertEquals(10, count);
    }

    @Test
    public void Test2() throws SQLException {
        for (int i = 1; i <= 10; i++) {
            String sql = "INSERT INTO public.animal " +
                    "(id, \"name\", age, \"type\", sex, place) " +
                    "VALUES(" + i + ", 'Бусинка', 2, 1, 1, 1);";
            assertThrows(SQLException.class, () -> executeUpdate(sql));
        }

    }

    private int executeUpdate(final String query) throws SQLException {
        Statement statement = connection.createStatement();
        // Для Insert, Update, Delete
        int result = statement.executeUpdate(query);
        return result;
    }

    @Test
    public void Test3() {
        String sql = "INSERT INTO public.workman " +
                "(id, \"name\", age, \"position\") " +
                "VALUES(1, " + null + ", 23, 1);";
        assertThrows(SQLException.class, () -> executeUpdate(sql));
    }

    @Test
    public void Test4() throws SQLException {
        String sql = "INSERT INTO public.places (id, \"row\", place_num, \"name\") VALUES(6, 1, 185, 'Загон 1');";
        assertEquals(1, executeUpdate(sql));
        sql = "SELECT count(id) as count FROM public.places;";
        PreparedStatement statement = connection.prepareStatement(sql);
        boolean hasResult = statement.execute();
        assertTrue(hasResult);
        ResultSet set = statement.getResultSet();
        set.next();
        int count = set.getInt("count");
        assertEquals(6, count);
    }

    @Test
    public void Test5() throws SQLException {
        String sql = "SELECT count(name) as count from public.zoo where name in ('Центральный', 'Северный', 'Западный');";
        PreparedStatement statement = connection.prepareStatement(sql);
        boolean hasResult = statement.execute();
        assertTrue(hasResult);
        ResultSet set = statement.getResultSet();
        set.next();
        int count = set.getInt("count");
        assertEquals(3, count);
    }
}
