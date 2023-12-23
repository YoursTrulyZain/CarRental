package carsharing.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {

    public Connection getConnection() throws SQLException {
        String jdbcUrl = "jdbc:h2:./src/carsharing/db/carsharing";
        String username = "";
        String password = "";
        return DriverManager.getConnection(jdbcUrl, username, password);
    }

    private void enableAutoCommit(Connection connection) {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PreparedStatement prepareStatement(String query) throws SQLException {
        Connection connection = getConnection();
        enableAutoCommit(connection);
        return connection.prepareStatement(query);
    }

    public PreparedStatement prepareStatementWithKeys(String query) throws SQLException {
        Connection connection = getConnection();
        enableAutoCommit(connection);
        return connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
    }
}
