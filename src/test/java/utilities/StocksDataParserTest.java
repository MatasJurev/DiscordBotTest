package utilities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StocksDataParserTest {

    @Test
    void getSymbols() {
        String data = "{\"symbol\":\"GOOGL\",\"name\":\"Alphabet Inc. Class A Common Stock\",\"lastsale\":\"$2774.39\",\"netchange\":\"-41.61\",\"pctchange\":\"-1.478%\",\"marketCap\":\"1,849,838,138,109\",\"url\":\"/market-activity/stocks/googl\"},{\"symbol\":\"AMZN\",\"name\":\"Amazon.com, Inc. Common Stock\",\"lastsale\":\"$3355.73\",\"netchange\":\"-106.79\",\"pctchange\":\"-3.084%\",\"marketCap\":\"1,699,477,646,180\",\"url\":\"/market-activity/stocks/amzn\"},{\"symbol\":\"FB\",\"name\":\"Facebook, Inc. Class A Common Stock\",\"lastsale\":\"$355.70\",\"netchange\":\"-9.02\",\"pctchange\":\"-2.473%\",\"marketCap\":\"1,002,876,409,006\",\"url\":\"/market-activity/stocks/fb\"},";

        String[] actual = StocksDataParser.getSymbols(data);
        String[] expected = new String[]{ "GOOGL", "AMZN", "FB" };

        for(int i=0; i < actual.length; i++) {

            assertEquals(expected[i], actual[i]);
        }
    }
}