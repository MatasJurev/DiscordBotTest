package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteDataSource {

    public static void init() throws SQLException {
        String jdbcUrl = "jdbc:sqlite:database.db";
        Connection connection = DriverManager.getConnection(jdbcUrl);
        String sql = "CREATE TABLE IF NOT EXISTS commands (ID INTEGER PRIMARY KEY AUTOINCREMENT, count INTEGER, command TEXT);";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        connection.close();
    }
}
