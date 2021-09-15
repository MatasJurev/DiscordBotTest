package utilities;

import yahoofinance.Stock;
import yahoofinance.quotes.stock.StockQuote;

public class StringUtils {

    public static String stockAsString(Stock stock) {
        StringBuilder sb = new StringBuilder();
        StockQuote quote = stock.getQuote();

        sb.append("Price: ");
        sb.append(quote.getPrice());
        sb.append(" USD");
        sb.append(", change: ");
        sb.append(quote.getChange() + "%");

        return sb.toString();
    }

    public static String stockAsStringWithName(Stock stock) {
        StringBuilder sb = new StringBuilder();
        StockQuote quote = stock.getQuote();

        sb.append("Name: ");
        sb.append(stock.getName());
        sb.append(" [" + stock.getSymbol() + "]");
        sb.append(", price: ");
        sb.append(quote.getPrice());
        sb.append(" USD");
        sb.append(", change: ");
        sb.append(quote.getChange() + "%");

        return sb.toString();
    }

    public static String cryptoAsString(Stock crypto) {
        StringBuilder sb = new StringBuilder();
        StockQuote quote = crypto.getQuote();

        sb.append("Price: ");
        sb.append(quote.getPrice());
        sb.append(" USD");
        sb.append(", year low: ");
        sb.append(quote.getYearLow());
        sb.append(", year high: ");
        sb.append(quote.getYearHigh());
        sb.append(", change: ");
        sb.append(quote.getChange() + "%");

        return sb.toString();
    }

    public static String cryptoAsStringWithName(Stock crypto) {

        StringBuilder sb = new StringBuilder();
        StockQuote quote = crypto.getQuote();

        sb.append("Name: ");
        sb.append(crypto.getName());
        sb.append(", price: ");
        sb.append(quote.getPrice());
        sb.append(" USD");
        sb.append(", change: ");
        sb.append(quote.getChange() + "%");

        return sb.toString();
    }
}
