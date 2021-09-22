package utilities;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;

import java.io.IOException;

public class WebscrapingUtils {

    /**
     * Webscrapes top stocks from the nasdaq.com api and passes the data to StocksDataParser
     * @return a String array of stocks' symbols
     * @throws IOException
     */
    public static String[] getTopStocks() throws IOException {
        WebClient client = new WebClient();
        String baseUrl = "https://api.nasdaq.com/api/screener/stocks?tableonly=true&limit=" + 10 + "&exchange=NASDAQ";

        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setCssEnabled(false);

        Page page = client.getPage(baseUrl);
        String content = page.getWebResponse().getContentAsString();

        return StocksDataParser.getSymbols(content);
    }


    /**
     * Webscrapes top cryptocurrencies from the bitfinex.com api and passes the data to CryptoDataParser
     * @return a String array of cryptocurrencies' symbols
     * @throws IOException
     */
    public static String[] getTopCryptos() throws IOException {
        WebClient client = new WebClient();
        String baseUrl = "https://api-pub.bitfinex.com/v2/conf/pub:list:currency";

        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setCssEnabled(false);

        Page page = client.getPage(baseUrl);
        String content = page.getWebResponse().getContentAsString();

        return CryptoDataParser.getSymbols(content);
    }
}
