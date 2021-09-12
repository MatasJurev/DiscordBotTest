package discord.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class DiscordBot {

    public static void init() throws LoginException {
        JDA jda = JDABuilder.createDefault(System.getenv("TOKEN"))
                .addEventListeners(new Listener())
                .setActivity(Activity.watching("currencies and cryptocurrencies"))
                .build();
    }

}
