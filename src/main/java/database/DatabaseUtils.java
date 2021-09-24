package database;

import utilities.StringUtils;

import java.sql.*;

public class DatabaseUtils {

    /**
     *
     * @param jdbcUrl
     * @return sum of all the commands' counts
     * @throws SQLException
     */
    public static double getTotal(String jdbcUrl) throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcUrl);
        String select = "SELECT SUM(count) FROM commands;";
        Statement statement = connection.createStatement();
        double result = statement.executeQuery(select).getDouble("SUM(count)");
        connection.close();
        return result;
    }


    /**
     * connects to database and gets top used commands by the bot as a String
     * @return top used commands by the bot as a String
     * @throws SQLException
     */
    public static String getStats() throws SQLException {
        String jdbcUrl = "jdbc:sqlite:database.db";
        Connection connection = DriverManager.getConnection(jdbcUrl);
        String select = "SELECT ID, command, count FROM commands ORDER BY count DESC;";
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(select);
        String resultString = StringUtils.topComString(result);
        connection.close();

        return resultString;
    }


    /**
     * adds or updates a certain command in the database
     * @param id
     * @param command
     * @param jdbcUrl
     * @throws SQLException
     */
    public static void addOrUpdateCommand(int id, String command, String jdbcUrl) throws SQLException {
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


    /**
     *
     * @param statement
     * @param id
     * @return from database the amount of times a command with the certain id was used
     * @throws SQLException
     */
    public static int getCountFromCommandsTable(Statement statement, int id) throws SQLException {
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
