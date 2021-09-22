package database;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseUtilsTest {

    @Test
    void getTotal() throws SQLException {
        double actual = DatabaseUtils.getTotal("jdbc:sqlite:mockDB.db");
        double expected = 0;
        Connection connection = DriverManager.getConnection("jdbc:sqlite:mockDB.db");
        Statement statement;

        for(int id=0; id<10; id++) {
            statement = connection.createStatement();

            expected += DatabaseUtils.getCountFromCommandsTable(statement, id);
        }

        connection.close();
        assertEquals(expected, actual);
    }

    @Test
    void addOrUpdateCommand() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:mockDB.db");
        Statement statement = connection.createStatement();

        int countBefore = DatabaseUtils.getCountFromCommandsTable(statement, 4);
        connection.close();

        DatabaseUtils.addOrUpdateCommand(4,"crypto","jdbc:sqlite:mockDB.db");

        connection = DriverManager.getConnection("jdbc:sqlite:mockDB.db");
        statement = connection.createStatement();
        int countAfter = DatabaseUtils.getCountFromCommandsTable(statement, 4);

        connection.close();
        assertEquals(countBefore + 1, countAfter);
    }

    @Test
    void getCountFromCommandsTable() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:mockDB.db");
        Statement statement = connection.createStatement();

        int actual = DatabaseUtils.getCountFromCommandsTable(statement, 9);
        connection.close();
        assertEquals(4, actual);
    }
}