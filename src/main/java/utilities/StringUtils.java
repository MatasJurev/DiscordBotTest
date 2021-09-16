package utilities;

import yahoofinance.Stock;
import yahoofinance.quotes.stock.StockQuote;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StringUtils {

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

    public static String stockAsStringWithName(Stock stock) {
        StringBuilder sb = new StringBuilder();
        StockQuote quote = stock.getQuote();

        sb.append("-"+stock.getName());
        sb.append(" (" + stock.getSymbol() + ")");
        sb.append(",   price: ");
        sb.append(quote.getPrice());
        sb.append(" USD");
        sb.append(",   Daily change: ");
        sb.append(quote.getChange() + "%");
        return sb.toString();
    }

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

    public static String cryptoAsStringWithName(Stock crypto) {

        StringBuilder sb = new StringBuilder();
        StockQuote quote = crypto.getQuote();

        sb.append("-"+crypto.getName());
        sb.append(",   price: ");
        sb.append(quote.getPrice());
        sb.append(" USD");
        sb.append(",   Daily change: ");
        sb.append(quote.getChange() + "%");

        return sb.toString();
    }
    public static String convertDate(Calendar cal){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate=format.format(cal.getTime());
        return formatDate;
    }

}
