package utilities;

import database.DatabaseUtils;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    void convertDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2000);
        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String actual = StringUtils.convertDate(calendar);

        assertEquals("2000-02-01", actual);
    }

    /*@Test
    void topComString() throws SQLException {
        String jdbcUrl = "jdbc:sqlite:mockDB.db";
        Connection connection = DriverManager.getConnection(jdbcUrl);
        String select = "SELECT ID, command, count FROM commands ORDER BY count DESC;";
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(select);
        String actual = StringUtils.topComString(result);

        StringBuilder expected = new StringBuilder();
        double total = DatabaseUtils.getTotal(jdbcUrl);
        result = statement.executeQuery(select);
        while(result.next()) {
            expected.append(" - $"+ result.getString("command")+" --- "+
                            result.getInt       ("count")+" ("+
                            Math.round((result.getDouble("count") / total * 100)) + "%)"+
                            System.lineSeparator()); }

        connection.close();
        assertEquals(expected.toString(), actual);
    }*/
}