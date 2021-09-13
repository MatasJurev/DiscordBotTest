package utilities;

import yahoofinance.Stock;
import yahoofinance.quotes.stock.StockQuote;

public class StringUtils {

    public static String stockAsString(Stock stock) {
        StringBuilder sb = new StringBuilder();
        StockQuote quote = stock.getQuote();

        sb.append("Price: ");
        sb.append(quote.getPrice());
        sb.append(", year low: ");
        sb.append(quote.getYearLow());
        sb.append(", year high: ");
        sb.append(quote.getYearHigh());
        sb.append(", change: ");
        sb.append(quote.getChange());
        sb.append(", currency: ");
        sb.append(stock.getCurrency());
        sb.append(", bid: ");
        sb.append(quote.getBid());

        return sb.toString();
    }
}
