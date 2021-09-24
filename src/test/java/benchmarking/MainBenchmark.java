package benchmarking;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import utilities.CryptoDataParser;
import utilities.StocksDataParser;
import utilities.StringUtils;
import utilities.WebscrapingUtils;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 1)
@Measurement(iterations = 3)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
public class MainBenchmark {

    @State(Scope.Thread)
    public static class StateVariables {
        public static int N;
        public static String parsersData;
        public static Calendar calendar;
        public static Stock stock;
        public static Stock crypto;
        public static Collection<Stock> cryptosForTop10;

        //Tell JMH that this method should be called to setup the state object before it
        //is passed to the benchmark method.
        @Setup(Level.Trial)
        public void doSetup() throws IOException {
            N = 10000;
            calendar = new GregorianCalendar();
            stock = YahooFinance.get("ACN");
            crypto  = YahooFinance.get("BTC-USD");
            parsersData = "data\":{\"filters\":null,\"table\":{\"headers\":{\"symbol\":\"Symbol\",\"name\":\"Name";
            String[] symbols = WebscrapingUtils.getTopCryptos();
            cryptosForTop10 = YahooFinance.get(symbols).values();
        }
    }


    public static void main(String[] args) throws RunnerException {

        Options opt = new OptionsBuilder()
                .include(MainBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Benchmark
    public void stringUtilsConvertDate() {

        for(int i=0; i < StateVariables.N; i++) {
            StringUtils.convertDate(StateVariables.calendar);
        }
    }

    @Benchmark
    public void stringUtilsCryptoAsString() {

        for(int i=0; i < StateVariables.N; i++){
            StringUtils.cryptoAsString(StateVariables.crypto);
        }
    }

    @Benchmark
    public void stringUtilsStockAsString() {

        for(int i=0; i < StateVariables.N; i++){
            StringUtils.stockAsString(StateVariables.stock);
        }
    }

    @Benchmark
    public void stringUtilsDividendString() {

        for(int i=0; i < StateVariables.N; i++){
            StringUtils.dividendString(StateVariables.stock);
        }
    }

    @Benchmark
    public void stocksDataParserGetSymbols() {

        for(int i=0; i < StateVariables.N; i++) {
            StocksDataParser.getSymbols(StateVariables.parsersData);
        }
    }

    @Benchmark
    public void cryptoDataParserGetSymbols() {

        for(int i=0; i < StateVariables.N; i++) {
            CryptoDataParser.getSymbols(StateVariables.parsersData);
        }
    }

    @Benchmark
    public void cryptoDataParserGetTop10() {

        for(int i=0; i < StateVariables.N; i++) {
            CryptoDataParser.getTop10(StateVariables.cryptosForTop10);
        }
    }
}
