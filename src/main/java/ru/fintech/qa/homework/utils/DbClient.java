package ru.fintech.qa.homework.utils;

import java.sql.*;

public class DbClient {
    private Connection connection;

    public DbClient() throws SQLException {
        String url = "jdbc:h2:mem:myDb";
        String user = "sa";
        String passwd = "sa";
        connection = DriverManager.getConnection(url, user, passwd);
    }

    public Connection getConnection() {
        return connection;
    }

    public int executeUpdate(final String query) throws SQLException {
        Statement statement = connection.createStatement();
        // Для Insert, Update, Delete
        int result = statement.executeUpdate(query);
        return result;
    }

    public PreparedStatement getStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }
}
