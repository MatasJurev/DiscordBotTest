package utilities;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class StocksDataParser {

    /**
     * Parses all the symbols of stocks from the json String
     * @param data
     * @return symbols of stocks
     */
    public static String[] getSymbols(String data) {

        //String[] split = data.split("[\":,{}]");
        String[] split = data.split("[\\[\\]\":,{}<>=]");
        List<String> parsedData = new LinkedList<>();
        List<String> answerList = new LinkedList<>();

        Collections.addAll(parsedData, split);
        parsedData.removeIf(item -> item == null || "".equals(item));

        for(int i=0; i<parsedData.size(); i++) {

            if(parsedData.get(i).equalsIgnoreCase("symbol")) {
                answerList.add(parsedData.get(i+1));
            }
        }

        String[] answer = new String[answerList.size()];
        answer = answerList.toArray(answer);

        return answer;
    }
}
