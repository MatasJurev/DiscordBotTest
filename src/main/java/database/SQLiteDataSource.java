package database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteDataSource {

    private static final HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    static {
        try {
            final File dbFile = new File("database.db");
            if(!dbFile.exists()) {
                if(dbFile.createNewFile()) {
                    System.out.println("Created database file");
                }
                else {
                    System.out.println("Could not create a database file");
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        config.setJdbcUrl("jdbc:sqlite:database.db");
        config.setConnectionTestQuery("SELECT 1");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);

        try (final Statement statement = getConnection().createStatement()){
            // language=SQLite
            statement.execute("CREATE TABLE IF NOT EXISTS commands (id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "command NOT NULL);");

            System.out.println("Table initialised");
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private SQLiteDataSource() {}

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
