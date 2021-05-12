package ru.fintech.qa.homework;

import org.junit.jupiter.api.*;
import ru.fintech.qa.homework.utils.BeforeUtils;
import ru.fintech.qa.homework.utils.DbClient;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestSQL {

    private DbClient client;

    @BeforeAll
    static void staticInit() {
        BeforeUtils.createData();
    }

    @BeforeAll
    public void init() throws SQLException {
        client = new DbClient();
    }

    @AfterAll
    public void close() throws SQLException {
        client.getConnection().close();
    }

    @Test
    public void Test1() throws SQLException {
        String sql = "SELECT count(id) as count FROM public.animal;";
        PreparedStatement statement = client.getStatement(sql);
        boolean hasResult = statement.execute();
        assertTrue(hasResult);
        ResultSet set = statement.getResultSet();
        set.next();
        int count = set.getInt("count");
        assertEquals(10, count);
    }

    @Test
    public void Test2() {
        for (int i = 1; i <= 10; i++) {
            String sql = "INSERT INTO public.animal " +
                    "(id, \"name\", age, \"type\", sex, place) " +
                    "VALUES(" + i + ", 'Бусинка', 2, 1, 1, 1);";
            assertThrows(SQLException.class, () -> client.executeUpdate(sql));
        }
    }

    @Test
    public void Test3() {
        String sql = "INSERT INTO public.workman " +
                "(id, \"name\", age, \"position\") " +
                "VALUES(1, " + null + ", 23, 1);";
        assertThrows(SQLException.class, () -> client.executeUpdate(sql));
    }

    @Test
    public void Test4() throws SQLException {
        String sql = "INSERT INTO public.places (id, \"row\", place_num, \"name\") VALUES(6, 1, 185, 'Загон 1');";
        assertEquals(1, client.executeUpdate(sql));
        sql = "SELECT count(id) as count FROM public.places;";
        PreparedStatement statement = client.getStatement(sql);
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
        PreparedStatement statement = client.getStatement(sql);
        boolean hasResult = statement.execute();
        assertTrue(hasResult);
        ResultSet set = statement.getResultSet();
        set.next();
        int count = set.getInt("count");
        assertEquals(3, count);
    }
}
