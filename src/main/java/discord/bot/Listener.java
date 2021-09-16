package discord.bot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import utilities.CryptoDataParser;
import utilities.StringUtils;
import utilities.WebscrapingUtils;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.quotes.fx.FxQuote;

import java.awt.*;
import java.io.IOException;
import java.util.Collection;


public class Listener extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        System.out.println("Logged in as: " + event.getJDA().getSelfUser().getAsTag());
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        //JDA jda = event.getJDA();                       //JDA, the core of the api.
        //long responseNumber = event.getResponseNumber();//The amount of discord events that JDA has received since the last reconnect.

        //Event specific information
        User author = event.getAuthor();                //The user that sent the message
        Message message = event.getMessage();           //The message that was received.
        MessageChannel channel = event.getChannel();    //This is the MessageChannel that the message was sent to.

        String msg = message.getContentDisplay();              //This returns a human readable version of the Message. Similar to
        // what you would see in the client.

        boolean bot = author.isBot();                    //This boolean is useful to determine if the User that
        // sent the Message is a BOT or not!

        if (event.isFromType(ChannelType.TEXT))         //If this message was sent to a Guild TextChannel
        {
            //Because we now know that this message was sent in a Guild, we can do guild specific things
            // Note, if you don't check the ChannelType before using these methods, they might return null due
            // the message possibly not being from a Guild!

            Guild guild = event.getGuild();             //The Guild that this message was sent in. (note, in the API, Guilds are Servers)
            TextChannel textChannel = event.getTextChannel(); //The TextChannel that this message was sent to.
            Member member = event.getMember();          //This Member that sent the message. Contains Guild specific information about the User!

            String name;
            if (message.isWebhookMessage())
            {
                name = author.getName();                //If this is a Webhook message, then there is no Member associated
            }                                           // with the User, thus we default to the author for name.
            else
            {
                name = member.getEffectiveName();       //This will either use the Member's nickname if they have one,
            }                                           // otherwise it will default to their username. (User#getName())

            System.out.printf("(%s)[%s]<%s>: %s\n", guild.getName(), textChannel.getName(), name, msg);
        }
        else if (event.isFromType(ChannelType.PRIVATE)) //If this message was sent to a PrivateChannel
        {
            //The message was sent in a PrivateChannel.
            //In this example we don't directly use the privateChannel, however, be sure, there are uses for it!
            //PrivateChannel privateChannel = event.getPrivateChannel();

            System.out.printf("[PRIV]<%s>: %s\n", author.getName(), msg);
        }

        try {
            if (msg.startsWith("$") && !bot) {
                msg = msg.substring(1).toLowerCase();
                EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(Color.red);

                if (msg.equalsIgnoreCase("help")) {
                    eb.setTitle("Help");
                    eb.setFooter("Usage: \"!cer Currency1 Currency2\" - gets exchange rate between two currencies." +
                            " Use \"!list currencies\" to see all available currencies");
                    channel.sendMessage(eb.build()).queue();
                }
                else if (msg.startsWith("cer")) {
                    String[] splitted = msg.split("\\s+");
                    msg = splitted[1] + splitted[2];
                    FxQuote currency = YahooFinance.getFx(msg.toUpperCase() + "=X");

                    String title  = currency.getSymbol();

                    eb.setTitle(title.substring(0, 3) + " to " + title.substring(3, 6) + " exchange rate:");
                    eb.setFooter(String.valueOf(currency.getPrice()));
                    channel.sendMessage(eb.build()).queue();
                }
                else if (msg.startsWith("stock")) {
                    msg = msg.split("\\s+")[1];
                    Stock stock = YahooFinance.get(msg.toUpperCase());

                    eb.setTitle(stock.getName());
                    eb.setFooter(StringUtils.stockAsString(stock));
                    channel.sendMessage(eb.build()).queue();
                }
                else if (msg.startsWith("crypto")) {
                    msg = msg.split("\\s+")[1];
                    Stock stock = YahooFinance.get(msg.toUpperCase() + "-USD");

                    eb.setTitle(stock.getName());
                    eb.setFooter(StringUtils.cryptoAsString(stock));
                    channel.sendMessage(eb.build()).queue();
                }
                else if(msg.startsWith("top stocks")) {
                    String[] symbols = WebscrapingUtils.getTopStocks();
                    Collection<Stock> stocks = YahooFinance.get(symbols).values();

                    for(Stock stock : stocks) {
                        if(stock.getName() != null) {
                            eb.addField(stock.getName(), StringUtils.cryptoAsString(stock), false);
                        }
                    }

                    eb.setTitle("Top 10 stocks:");
                    channel.sendMessage(eb.build()).queue();
                }
                else if(msg.startsWith("top cryptos")) {
                    String[] symbols = WebscrapingUtils.getTopCryptos();
                    Collection<Stock> cryptos = YahooFinance.get(symbols).values();
                    cryptos = CryptoDataParser.getTop10(cryptos);

                    for(Stock crypto : cryptos) {
                        eb.addField(crypto.getName(), StringUtils.cryptoAsString(crypto), false);
                    }

                    eb.setTitle("Top 10 cryptocurrencies by price:");
                    channel.sendMessage(eb.build()).queue();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}