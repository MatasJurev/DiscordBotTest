package utilities;

import yahoofinance.Stock;

import java.util.*;
import java.util.stream.Collectors;

public class CryptoDataParser {

    public static String[] getSymbols(String data) {

        //String[] split = data.split("[\":,{}<>\\-]");
        String[] split = data.split("[-]");
        List<String> parsedData = new LinkedList();
        List<String> answerList = new LinkedList();

        Collections.addAll(parsedData, split);
        parsedData.removeIf(item -> item == null || "".equals(item));

        for(int i=0; i<parsedData.size(); i++) {

            if(parsedData.get(i).equalsIgnoreCase("symbol")) {
                answerList.add(parsedData.get(i+4).toUpperCase() + "-USD");
                System.out.println(parsedData.get(i+4).toUpperCase());
            }
        }

        //remove possible duplicates
        answerList = answerList.stream().distinct().collect(Collectors.toList());

        String[] answer = new String[answerList.size()];
        answer = answerList.toArray(answer);

        return answer;
    }

    public static Collection<Stock> getTop10(Collection<Stock> cryptos) {
        List top10 = new ArrayList(cryptos);

        top10.removeIf(item -> ((Stock)item).getName() == null || "".equals(((Stock)item).getName()));
        top10.sort(new CryptoComparator());
        top10 = top10.subList(0, 10);

        return top10;
    }
}
