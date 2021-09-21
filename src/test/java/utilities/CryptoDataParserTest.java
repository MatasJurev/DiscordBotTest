package utilities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CryptoDataParserTest {

    @Test
    void getSymbols() {
        String data = "[[,BTC, ETH, ADA[]],,,,]]";
        String[] expected = new String[] {"BTC-USD", "ETH-USD", "ADA-USD"};
        String[] actual = CryptoDataParser.getSymbols(data);

        for(int i=0; i< actual.length; i++) {

            assertEquals(expected[i], actual[i]);
        }
    }

    @Test
    void getTop10() {

    }
}