package discord.bot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.quotes.fx.FxQuote;

import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Map;

public class Listener extends ListenerAdapter {

    List<String> test = new ArrayList<>();

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
            if (message.isWebhookMessage()) {
                name = author.getName();                //If this is a Webhook message, then there is no Member associated
            }                                           // with the User, thus we default to the author for name.
            else {
                name = member.getEffectiveName();       //This will either use the Member's nickname if they have one,
            }                                           // otherwise it will default to their username. (User#getName())

            System.out.printf("(%s)[%s]<%s>: %s\n", guild.getName(), textChannel.getName(), name, msg);
        } else if (event.isFromType(ChannelType.PRIVATE)) //If this message was sent to a PrivateChannel
        {
            //The message was sent in a PrivateChannel.
            //In this example we don't directly use the privateChannel, however, be sure, there are uses for it!
            PrivateChannel privateChannel = event.getPrivateChannel();

            System.out.printf("[PRIV]<%s>: %s\n", author.getName(), msg);
        }

        else if (msg.equals("!display") && !bot) {
            channel.sendMessage(test.toString()).queue();
        }


       ///FOR STOCKS + CRYPTO, use ! XXX
        try {
            if (msg.startsWith("!") && !bot) {
                msg = msg.substring(1).toLowerCase();
                EmbedBuilder eb = new EmbedBuilder();
                msg = msg.split("\\s+")[1];

                Stock stock = YahooFinance.get(msg.toUpperCase());
                String titleString =  stock.getName();
                String symbolString =  stock.getSymbol();
                eb.setTitle("The Exchange rate of: " + titleString +"("+symbolString+")"+" is: " + (stock.getQuote(true).getPrice()));
                eb.setColor(Color.red);
                channel.sendMessage(eb.build()).queue();
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        ///FOR CURRENCIES, use $ xxxXXX
        try {
            if (msg.startsWith("$") && !bot) {
                msg = msg.substring(1).toLowerCase();
                EmbedBuilder eb = new EmbedBuilder();
                msg = msg.split("\\s+")[1];

                    FxQuote currency = YahooFinance.getFx(msg.toUpperCase() + "=X");
                    String titleString =  currency.getSymbol();
                    titleString = titleString.substring(0, titleString.length() - 2);
                    eb.setTitle("The Exchange rate of: " + titleString + " is: " + currency.getPrice());
                    eb.setColor(Color.blue);
                    channel.sendMessage(eb.build()).queue();
                }

            } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        try {
        if (msg.startsWith("#Crypto") && !bot){
            EmbedBuilder eb = new EmbedBuilder();
            String[] symbols = new String[] {"BTC-USD", "ETH-USD", "ADA-USD","BNB-USD","XRP-USD"};
            Map<String, Stock> stocks = YahooFinance.get(symbols);
            Stock stock1 = stocks.get(symbols[0]);
            Stock stock2 = stocks.get(symbols[1]);
            Stock stock3 = stocks.get(symbols[2]);
            Stock stock4 = stocks.get(symbols[3]);
            Stock stock5 = stocks.get(symbols[4]);
            eb.setTitle("The Top Cryptocurrency Prices right now: ");
            String text =
                    (stock1.getName() + " trades @ " + stock1.getQuote(true).getPrice() + System.lineSeparator() +
                    stock2.getName() + " trades @ " + stock2.getQuote(true).getPrice() + System.lineSeparator() +
                    stock3.getName() + " trades @ " + stock3.getQuote(true).getPrice() + System.lineSeparator() +
                    stock4.getName() + " trades @ " + stock4.getQuote(true).getPrice() + System.lineSeparator() +
                    stock5.getName() + " trades @ " + stock5.getQuote(true).getPrice());
            eb.setFooter(text);
            eb.setColor(Color.red);
            channel.sendMessage(eb.build()).queue();;
        }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        try {
            if (msg.startsWith("#Stocks") && !bot){
                EmbedBuilder eb = new EmbedBuilder();
                String[] symbols = new String[] {"AAPL", "MSFT", "GOOG","AMZN","FB"};
                Map<String, Stock> stocks = YahooFinance.get(symbols);
                Stock stock1 = stocks.get(symbols[0]);
                Stock stock2 = stocks.get(symbols[1]);
                Stock stock3 = stocks.get(symbols[2]);
                Stock stock4 = stocks.get(symbols[3]);
                Stock stock5 = stocks.get(symbols[4]);
                eb.setTitle("The Top Stock Prices right now: ");
                String text =
                        (stock1.getName() + " trades @ " + stock1.getQuote(true).getPrice() + System.lineSeparator() +
                                stock2.getName() + " trades @ " + stock2.getQuote(true).getPrice() + System.lineSeparator() +
                                stock3.getName() + " trades @ " + stock3.getQuote(true).getPrice() + System.lineSeparator() +
                                stock4.getName() + " trades @ " + stock4.getQuote(true).getPrice() + System.lineSeparator() +
                                stock5.getName() + " trades @ " + stock5.getQuote(true).getPrice());
                eb.setFooter(text);
                eb.setColor(Color.red);
                channel.sendMessage(eb.build()).queue();;
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        try {
            if (msg.startsWith("#Forex") && !bot){
                EmbedBuilder eb = new EmbedBuilder();

                String[] symbols = new String[] {"EURUSD=X", "GBPUSD=X", "USDJPY=X","AUDUSD=X","USDCAD=X"};
                Map<String, FxQuote> currencies = YahooFinance.getFx(symbols);
                FxQuote stock1 = currencies.get(symbols[0]);
                FxQuote stock2 = currencies.get(symbols[1]);
                FxQuote stock3 = currencies.get(symbols[2]);
                FxQuote stock4 = currencies.get(symbols[3]);
                FxQuote stock5 = currencies.get(symbols[4]);
                eb.setTitle("The Top Currency Prices right now: ");
                
                 String text =(
                 (stock1.getSymbol().substring(0, stock1.getSymbol().length() - 2)) + " trades @ " + stock1.getPrice() + System.lineSeparator() + 
                 (stock2.getSymbol().substring(0, stock2.getSymbol().length() - 2)) + " trades @ " + stock2.getPrice() + System.lineSeparator() + 
                 (stock3.getSymbol().substring(0, stock3.getSymbol().length() - 2)) + " trades @ " + stock3.getPrice() + System.lineSeparator() +
                 (stock4.getSymbol().substring(0, stock4.getSymbol().length() - 2)) + " trades @ " + stock4.getPrice() + System.lineSeparator() +
                 (stock5.getSymbol().substring(0, stock5.getSymbol().length() - 2)) + " trades @ " + stock5.getPrice()
                 );
                eb.setFooter(text);
                eb.setColor(Color.red);
                channel.sendMessage(eb.build()).queue();;
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }


}


