package database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseUtils {

    public static String getCommand(int id) {

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection().prepareStatement(
                "SELECT command FROM commands"
        )){
            preparedStatement.setString(1, String.valueOf(id));
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }

        return "";
    }
}
