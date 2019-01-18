package com.dreampany.lca.data.misc;

import com.dreampany.frame.data.model.Flag;
import com.dreampany.frame.misc.SmartCache;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.lca.api.cmc.enums.CmcCurrency;
import com.dreampany.lca.api.cmc.model.CmcCoin;
import com.dreampany.lca.api.cmc.model.CmcQuote;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.Currency;
import com.dreampany.lca.data.model.Quote;
import com.dreampany.lca.data.source.api.CoinDataSource;
import com.dreampany.lca.misc.CoinAnnote;
import com.dreampany.lca.misc.QuoteAnnote;
import com.google.common.collect.Sets;
import org.jsoup.helper.StringUtil;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Hawladar Roman on 5/31/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class CoinMapper {

    private final Set<Coin> coins;
    private final SmartMap<Long, Coin> map;
    private final SmartCache<Long, Coin> cache;

    private final SmartMap<Long, Quote> quoteMap;
    private final SmartCache<Long, Quote> quoteCache;

    @Inject
    CoinMapper(@CoinAnnote SmartMap<Long, Coin> map,
               @CoinAnnote SmartCache<Long, Coin> cache,
               @QuoteAnnote SmartMap<Long, Quote> quoteMap,
               @QuoteAnnote SmartCache<Long, Quote> quoteCache) {
        coins = Sets.newConcurrentHashSet();
        this.map = map;
        this.cache = cache;
        this.quoteMap = quoteMap;
        this.quoteCache = quoteCache;
    }

    public boolean hasCoins() {
        return !coins.isEmpty();
    }

    public void add(Coin coin) {
        coins.add(coin);
    }

    public void add(List<Coin> coins) {
        if (!DataUtil.isEmpty(coins)) {
            for (Coin coin : coins) {
                add(coin);
            }
        }
    }

    public Coin getCoin(String symbol) {
        for (Coin coin : coins) {
            if (symbol.equals(coin.getSymbol())) {
                return coin;
            }
        }
        return null;
    }

    public List<Coin> getCoins() {
        return new ArrayList<>(coins);
    }

    public boolean isExists(Coin in) {
        return map.contains(in.getId());
    }

    public boolean isExists(Quote in) {
        return quoteMap.contains(in.getId());
    }

    public Coin toItem(CmcCoin in, boolean full) {
        if (in == null) {
            return null;
        }

        long id = DataUtil.getSha512(in.getSymbol(), in.getSlug());
        Coin out = map.get(id);
        if (out == null) {
            out = new Coin();
            if (full) {
                map.put(id, out);
            }
        }
        out.setId(id);
        out.setTime(TimeUtil.currentTime());
        out.setCoinId(in.getId());
        out.setName(in.getName());
        out.setSymbol(in.getSymbol());
        out.setSlug(in.getSlug());
        if (full) {
            out.setRank(in.getRank());
            out.setMarketPairs(in.getMarketPairs());
            out.setMaxSupply(in.getCirculatingSupply());
            out.setCirculatingSupply(in.getCirculatingSupply());
            out.setTotalSupply(in.getTotalSupply());
            out.setMaxSupply(in.getMaxSupply());
            out.setLastUpdated(in.getLastUpdatedTime());
            out.setDateAdded(in.getDateAddedTime());
            out.setTags(in.getTags());
        }
        bindQuote(out, in.getPriceQuote());
        return out;
    }

    public void bindQuote(Coin out, Map<CmcCurrency, CmcQuote> quotes) {
        for (Map.Entry<CmcCurrency, CmcQuote> entry : quotes.entrySet()) {
            Currency currency = toCurrency(entry.getKey());
            Quote quote = toQuote(out, currency, entry.getValue());
            out.addQuote(quote);
        }
    }

    public Currency toCurrency(CmcCurrency currency) {
        return Currency.valueOf(currency.name());
    }

    public Quote toQuote(Coin coin, Currency currency, CmcQuote in) {
        if (in == null) {
            return null;
        }
        long id = DataUtil.getSha512(coin.getId(), currency.name());
        Quote out = quoteMap.get(id);
        if (out == null) {
            out = new Quote();
            quoteMap.put(id, out);
        }
        out.setId(id);
        out.setTime(TimeUtil.currentTime());
        out.setCoinId(coin.getId());  //coin id to coinId of quote
        out.setCurrency(currency);
        out.setPrice(in.getPrice());
        out.setDayVolume(in.getDayVolume());
        out.setMarketCap(in.getMarketCap());
        out.setHourChange(in.getHourChange());
        out.setDayChange(in.getDayChange());
        out.setWeekChange(in.getWeekChange());
        out.setLastUpdated(in.getLastUpdated());
        return out;
    }

    public Coin toItem(Flag flag, CoinDataSource source) {
        Coin coin = map.get(flag.getId());
        if (coin == null) {
            coin = source.getItem(flag.getId());
            map.put(coin.getId(), coin);
        }
        return coin;
    }

    public String joinString(String[] values, String separator) {
        return StringUtil.join(values, separator);
    }

    public String join(Currency[] currencies, String separator) {
        StringBuilder builder = new StringBuilder();
        for (Currency currency : currencies) {
            DataUtil.joinString(builder, currency.name(), separator);
        }
        String currency = builder.toString();
        return currency;
    }

    public String[] toStringArray(Currency[] currencies) {
        String[] currency = new String[currencies.length];
        for (int index = 0; index < currencies.length; index++) {
            currency[index] = currencies[index].name();
        }
        return currency;
    }
}
