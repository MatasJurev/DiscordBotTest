package utilities;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;

import java.io.IOException;

public class WebscrapingUtils {

    public static String[] getTopStocks() throws IOException {
        WebClient client = new WebClient();
        String baseUrl = "https://api.nasdaq.com/api/screener/stocks?tableonly=true&limit=" + 10 + "&exchange=NASDAQ";

        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setCssEnabled(false);
        //client.getOptions().setUseInsecureSSL(true);

        Page page = client.getPage(baseUrl);
        String content = page.getWebResponse().getContentAsString();

        return StocksParser.getSymbols(content);
    }

}
