package utilities;

import yahoofinance.Stock;

import java.util.*;

public class CryptoDataParser {

    /**
     * parses cryptocurrencies' symbols from the String
     * @param data
     * @return cryptocurrencies' symbols as a String array
     */
    public static String[] getSymbols(String data) {

        String[] split = data.split("[\\[\\]\":,{}<>=\\s+]");
        List<String> parsedData = new LinkedList();
        List<String> answerList = new LinkedList();

        Collections.addAll(parsedData, split);
        parsedData.removeIf(item -> item == null || "".equals(item));

        for(int i=0; i<parsedData.size(); i++) {
            answerList.add(parsedData.get(i).toUpperCase() + "-USD");
        }

        String[] answer = new String[answerList.size()];
        answer = answerList.toArray(answer);

        return answer;
    }


    /**
     *
     * @param cryptos
     * @return a list of top 10 cryptocurrencies sorted by their market cap
     */
    public static List getTop10(Collection<Stock> cryptos) {
        List<Stock> top10 = new ArrayList(cryptos);

        top10.removeIf(item -> item.getName() == null || "".equals(item.getName()));
        top10.sort(new CryptoComparator().reversed());

        return top10.subList(0, 10);
    }
}
