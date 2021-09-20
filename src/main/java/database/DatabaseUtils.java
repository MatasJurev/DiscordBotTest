package database;

import java.sql.*;

public class DatabaseUtils {

    public static void addOrUpdateCommand(int id, String command) throws SQLException {
        String jdbcUrl = "jdbc:sqlite:database.db";
        Connection connection = DriverManager.getConnection(jdbcUrl);
        Statement statement = connection.createStatement();

        int count = getCountFromCommandsTable(statement, id);
        count++;

        statement = connection.createStatement();
        String sql = "INSERT OR IGNORE INTO commands (ID, count, command) VALUES (" + id  + ", " + count + ", '" + command + "');" +
                "UPDATE commands SET count = " + count + " WHERE command = '" + command + "';";
        statement.executeUpdate(sql);

        connection.close();
    }

    private static int getCountFromCommandsTable(Statement statement, int id) throws SQLException {
        String sql = "SELECT ID, count FROM commands;";
        ResultSet rs = statement.executeQuery(sql);

        while (rs.next()) {
            int currID = rs.getInt("ID");

            if(currID == id) {
                return rs.getInt("count");
            }
        }

        System.out.println("Warning: No such entry in commands table with id " + id + " was found. Adding a new entry.");
        return 0;
    }
}
