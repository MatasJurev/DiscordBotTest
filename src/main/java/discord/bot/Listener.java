package discord.bot;

import database.DatabaseUtils;
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
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.quotes.fx.FxQuote;

import java.awt.*;
import java.io.IOException;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import static utilities.StringUtils.convertDate;


public class Listener extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        System.out.println("Logged in as: " + event.getJDA().getSelfUser().getAsTag());
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        User author = event.getAuthor();
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();

        String msg = message.getContentDisplay();

        boolean bot = author.isBot();

        if (event.isFromType(ChannelType.TEXT))
        {
            Guild guild = event.getGuild();
            TextChannel textChannel = event.getTextChannel();
            Member member = event.getMember();

            String name;
            if (message.isWebhookMessage()) {
                name = author.getName();
            }
            else {
                name = member.getEffectiveName();
            }

            System.out.printf("(%s)[%s]<%s>: %s\n", guild.getName(), textChannel.getName(), name, msg);
        }
        else if (event.isFromType(ChannelType.PRIVATE))
        {
            System.out.printf("[PRIV]<%s>: %s\n", author.getName(), msg);
        }

        try {
            if (msg.startsWith("$") && !bot) {
                msg = msg.substring(1).toLowerCase();
                EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(Color.red);

                if (msg.startsWith("forex")) {
                    String[] splitted = msg.split("\\s+", 2);
                    msg = splitted[1];
                    FxQuote currency = YahooFinance.getFx(msg.toUpperCase() + "=X");

                    String title = currency.getSymbol();

                    eb.setTitle(title.substring(0, 6) + " exchange rate:");
                    eb.setFooter("Price: " + currency.getPrice());
                    channel.sendMessage(eb.build()).queue();

                    DatabaseUtils.addOrUpdateCommand(0, "forex", "jdbc:sqlite:database.db");
                }
                else if (msg.startsWith("stock")) {
                    msg = msg.split("\\s+")[1];
                    Stock stock = YahooFinance.get(msg.toUpperCase());

                    eb.setTitle(stock.getName() + " (" + stock.getSymbol() + ") " + "exchange rate:");
                    eb.setFooter(StringUtils.stockAsString(stock));
                    channel.sendMessage(eb.build()).queue();

                    DatabaseUtils.addOrUpdateCommand(1, "stock", "jdbc:sqlite:database.db");
                }
                else if (msg.startsWith("dividend")) {
                    msg = msg.split("\\s+")[1];
                    Stock stock = YahooFinance.get(msg.toUpperCase());

                    eb.setTitle(stock.getName() + " (" + stock.getSymbol() + ") " + "Dividend:");
                    eb.setFooter(StringUtils.dividendString(stock));
                    channel.sendMessage(eb.build()).queue();

                    DatabaseUtils.addOrUpdateCommand(2, "dividend", "jdbc:sqlite:database.db");
                }
                else if (msg.startsWith("eps")) {
                    msg = msg.split("\\s+")[1];
                    Stock stock = YahooFinance.get(msg.toUpperCase());

                    eb.setTitle(stock.getName() + " (" + stock.getSymbol() + ") " + "EPS:");
                    eb.setFooter("Earnings Per Share: " + (stock.getStats().getEps()) + "$");
                    channel.sendMessage(eb.build()).queue();

                    DatabaseUtils.addOrUpdateCommand(3, "eps", "jdbc:sqlite:database.db");
                }
                else if (msg.startsWith("crypto")) {
                    msg = msg.split("\\s+")[1];
                    Stock stock = YahooFinance.get(msg.toUpperCase() + "-USD");

                    eb.setTitle(stock.getName() + " exchange rate:");
                    eb.setFooter(StringUtils.cryptoAsString(stock));
                    channel.sendMessage(eb.build()).queue();

                    DatabaseUtils.addOrUpdateCommand(4, "crypto", "jdbc:sqlite:database.db");
                }
                else if (msg.startsWith("top stocks")) {
                    String[] symbols = WebscrapingUtils.getTopStocks();
                    Collection<Stock> stocks = YahooFinance.get(symbols).values();
                    StringBuilder sb = new StringBuilder();

                    for (Stock stock : stocks) {
                        if (stock.getName() != null) {
                            sb.append(StringUtils.stockAsStringWithName(stock));
                            sb.append("\n");
                        }
                    }

                    eb.setTitle("Top 10 stocks:");
                    eb.setFooter(sb.toString());
                    channel.sendMessage(eb.build()).queue();

                    DatabaseUtils.addOrUpdateCommand(5, "top stocks", "jdbc:sqlite:database.db");
                }
                else if (msg.startsWith("top cryptos")) {
                    StringBuilder sb = new StringBuilder();
                    String[] symbols = WebscrapingUtils.getTopCryptos();
                    Collection<Stock> cryptos = YahooFinance.get(symbols).values();
                    cryptos = CryptoDataParser.getTop10(cryptos);

                    for (Stock crypto : cryptos) {
                        sb.append(StringUtils.cryptoAsStringWithName(crypto));
                        sb.append("\n");
                    }

                    eb.setTitle("Top 10 Cryptocurrencies:");
                    eb.setFooter(sb.toString());
                    channel.sendMessage(eb.build()).queue();

                    DatabaseUtils.addOrUpdateCommand(6, "top cryptos", "jdbc:sqlite:database.db");
                }
                else if (msg.startsWith("history")) {
                    msg = msg.split("\\s+")[1];
                    Stock stock = YahooFinance.get(msg.toUpperCase());

                    StringBuilder footer = new StringBuilder();
                    List<HistoricalQuote> history = stock.getHistory();
                    footer.append(System.lineSeparator());
                    footer.append("DATE------PRICE------LOW------HIGH");
                    footer.append(System.lineSeparator());

                    for (HistoricalQuote quote : history) {
                        footer.append(convertDate(quote.getDate()));
                        footer.append("  |  " + quote.getClose().setScale(2, RoundingMode.HALF_UP));
                        footer.append("  |  " + quote.getLow().setScale(2, RoundingMode.HALF_UP));
                        footer.append("  |  " + quote.getHigh().setScale(2, RoundingMode.HALF_UP));
                        footer.append(System.lineSeparator());
                    }

                    eb.setTitle("Monthly Price History of " + stock.getName() + " (" + stock.getSymbol() + ")");
                    eb.setFooter(String.valueOf(footer));
                    channel.sendMessage(eb.build()).queue();

                    DatabaseUtils.addOrUpdateCommand(7, "history", "jdbc:sqlite:database.db");
                }
                else if (msg.startsWith("options")) {
                    eb.setTitle("Possible Options are:");
                    StringBuilder footer = new StringBuilder();

                    footer.append("$stock XXX...for Stocks " + System.lineSeparator());
                    footer.append("$crypto XXX...for Crypto " + System.lineSeparator());
                    footer.append("$forex XXX...for Currencies" + System.lineSeparator());
                    footer.append("$history XXX ...to get monthly price history  " + System.lineSeparator());
                    footer.append("$top stocks ... to get data about top 10 stocks  " + System.lineSeparator());
                    footer.append("$top cryptos ... to get data about top 10 cryptos  " + System.lineSeparator());
                    footer.append("$top commands ... to get a list of the most used commands  " + System.lineSeparator());

                    eb.setFooter(String.valueOf(footer));
                    eb.setColor(Color.red);
                    channel.sendMessage(eb.build()).queue();

                    DatabaseUtils.addOrUpdateCommand(8, "options", "jdbc:sqlite:database.db");
                }
                else if (msg.startsWith("hello")) {
                    StringBuilder footer = new StringBuilder();
                    footer.append("oooo    oooo    88oooo88" + System.lineSeparator());
                    footer.append(" 888      888          888    " + System.lineSeparator());
                    footer.append(" 88888888           888    " + System.lineSeparator());
                    footer.append(" 888      888          888    " + System.lineSeparator());
                    footer.append("oooo    oooo    88oooo88" + System.lineSeparator());
                    eb.setFooter(String.valueOf(footer));
                    eb.setColor(Color.red);
                    channel.sendMessage(eb.build()).queue();

                    DatabaseUtils.addOrUpdateCommand(9, "hello", "jdbc:sqlite:database.db");
                }
                else if(msg.startsWith("top commands")) {
                    eb.setTitle("Top Commands Used");
                    eb.setFooter(DatabaseUtils.getStats());
                    eb.setColor(Color.red);
                    channel.sendMessage(eb.build()).queue();
                }
            }
        }
        catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}