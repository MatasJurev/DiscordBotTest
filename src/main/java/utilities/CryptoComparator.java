package utilities;

import yahoofinance.Stock;

import java.util.Comparator;

public class CryptoComparator implements Comparator<Stock> {

    @Override
    public int compare(Stock s1, Stock s2) {
        return s1.getStats().getMarketCap().compareTo(s2.getStats().getMarketCap());
    }
}
