package utilities;

import database.DatabaseUtils;
import yahoofinance.Stock;
import yahoofinance.quotes.stock.StockDividend;
import yahoofinance.quotes.stock.StockQuote;

import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StringUtils {

    /**
     *
     * @param stock
     * @return information about stock as String
     */
    public static String stockAsString(Stock stock) {
        StringBuilder sb = new StringBuilder();
        StockQuote quote = stock.getQuote();

        sb.append("Price: ");
        sb.append(quote.getPrice());
        sb.append(" USD");
        sb.append(System.lineSeparator());
        sb.append("Daily change: ");
        sb.append(quote.getChange() + "%");

        return sb.toString();
    }


    /**
     *
     * @param stock
     * @return information about stock's dividend and annual yield as String
     */
    public static String dividendString(Stock stock) {
        StringBuilder sb = new StringBuilder();
        StockDividend dividend = stock.getDividend();

        sb.append("Dividend date: ");
        sb.append(dividend.getPayDate().getTime());
        sb.append(System.lineSeparator());
        sb.append("Annual yield: ");
        sb.append(dividend.getAnnualYield());
        sb.append(" (" + dividend.getAnnualYieldPercent().setScale(2, RoundingMode.HALF_UP) + "%)");

        return sb.toString();
    }


    /**
     *
     * @param stock
     * @return information about stock (including name) as String
     */
    public static String stockAsStringWithName(Stock stock) {
        StringBuilder sb = new StringBuilder();
        StockQuote quote = stock.getQuote();

        sb.append("-" + stock.getName());
        sb.append(" (" + stock.getSymbol() + ")");
        sb.append(",   price: ");
        sb.append(quote.getPrice());
        sb.append(" USD");
        sb.append(",   Daily change: ");
        sb.append(quote.getChange() + "%");
        return sb.toString();
    }


    /**
     *
     * @param crypto
     * @return information about cryptocurrency as String
     */
    public static String cryptoAsString(Stock crypto) {
        StringBuilder sb = new StringBuilder();
        StockQuote quote = crypto.getQuote();

        sb.append("Price: ");
        sb.append(quote.getPrice());
        sb.append(" USD");
        sb.append(System.lineSeparator());
        sb.append("Year low: ");
        sb.append(quote.getYearLow());
        sb.append(System.lineSeparator());
        sb.append("Year high: ");
        sb.append(quote.getYearHigh());
        sb.append(System.lineSeparator());
        sb.append("Yearly change: ");
        sb.append(quote.getChange() + "%");

        return sb.toString();
    }


    /**
     *
     * @param crypto
     * @return information about cryptocurrency (including name) as String
     */
    public static String cryptoAsStringWithName(Stock crypto) {

        StringBuilder sb = new StringBuilder();
        StockQuote quote = crypto.getQuote();

        sb.append("-" + crypto.getName());
        sb.append(",   price: ");
        sb.append(quote.getPrice());
        sb.append(" USD");
        sb.append(",   Daily change: ");
        sb.append(quote.getChange() + "%");

        return sb.toString();
    }


    /**
     *
     * @param cal
     * @return date as a String in a "yyyy-MM-dd" format
     */
    public static String convertDate(Calendar cal) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate = format.format(cal.getTime());
        return formatDate;
    }

    /**
     *
     * @param result
     * @return top used commands by the bot as a String
     * @throws SQLException
     */
    public static String topComString(ResultSet result) throws SQLException {
        StringBuilder statsString = new StringBuilder();
        double total = DatabaseUtils.getTotal("jdbc:sqlite:database.db");
        while(result.next()) {
            statsString.append(
                    " - $"+ result.getString    ("command")+" --- "+
                            result.getInt       ("count")+" ("+
                            Math.round((result.getDouble("count") / total * 100)) + "%)"+
                            System.lineSeparator()
            );

        }
        return statsString.toString();
    }
}