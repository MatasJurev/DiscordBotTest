package discord.bot;

import database.SQLiteDataSource;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;

public class DiscordBot {

    public static void init() throws LoginException, SQLException {
        SQLiteDataSource.init();

        JDABuilder.createDefault(System.getenv("TOKEN"))
                .addEventListeners(new Listener())
                .setActivity(Activity.watching("currencies and cryptocurrencies"))
                .build();
    }
}
