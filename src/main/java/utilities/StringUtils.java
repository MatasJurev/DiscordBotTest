package utilities;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import yahoofinance.Stock;
import yahoofinance.quotes.stock.StockQuote;

import java.io.IOException;

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

    public static String cryptoAsString(Stock stock) {
        StringBuilder sb = new StringBuilder();
        StockQuote quote = stock.getQuote();

        sb.append("Price: ");
        sb.append(quote.getPrice());
        sb.append(" USD");
        sb.append(", year low: ");
        sb.append(quote.getYearLow());
        sb.append(", year high: ");
        sb.append(quote.getYearHigh());
        sb.append(", change: ");
        sb.append(quote.getChange());

        return sb.toString();
    }

    public static String[] getTopStocks() throws IOException {
        WebClient client = new WebClient();
        String baseUrl = "https://api.nasdaq.com/api/screener/stocks?tableonly=true&limit=" + 10 + "&exchange=NASDAQ";

        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setCssEnabled(false);
        client.getOptions().setUseInsecureSSL(true);

        Page page = client.getPage(baseUrl);
        String content = page.getWebResponse().getContentAsString();

        return StocksParser.getSymbols(content);
    }
}
