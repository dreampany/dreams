package com.dreampany.lca.data.misc;

import android.text.TextUtils;

import com.dreampany.frame.data.model.State;
import com.dreampany.frame.misc.SmartCache;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.lca.api.cmc.enums.CmcCurrency;
import com.dreampany.lca.api.cmc.model.CmcCoin;
import com.dreampany.lca.api.cmc.model.CmcQuote;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.Currency;
import com.dreampany.lca.data.model.Quote;
import com.dreampany.lca.data.source.api.CoinDataSource;
import com.dreampany.lca.misc.CoinAnnote;
import com.dreampany.lca.misc.Constants;
import com.dreampany.lca.misc.QuoteAnnote;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import hugo.weaving.DebugLog;

/**
 * Created by Hawladar Roman on 5/31/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class CoinMapper {

    private final SmartMap<Long, Coin> map;
    private final SmartCache<Long, Coin> cache;

    private final SmartMap<Long, Quote> quoteMap;
    private final SmartCache<Long, Quote> quoteCache;

    private final Map<Long, Coin> coins;

    @Inject
    CoinMapper(@CoinAnnote SmartMap<Long, Coin> map,
               @CoinAnnote SmartCache<Long, Coin> cache,
               @QuoteAnnote SmartMap<Long, Quote> quoteMap,
               @QuoteAnnote SmartCache<Long, Quote> quoteCache) {
        this.map = map;
        this.cache = cache;
        this.quoteMap = quoteMap;
        this.quoteCache = quoteCache;
        this.coins = Maps.newConcurrentMap();
    }

    public boolean hasCoins() {
        return !coins.isEmpty();
    }

    public boolean hasCoin(long coinId) {
        return coins.containsKey(coinId);
    }

    public boolean hasCoins(List<Long> coinIds) {
        for (long coinId : coinIds) {
            if (!hasCoin(coinId)) {
                return false;
            }
        }
        return true;
    }

    public void add(Coin coin) {
        //coins.put(coin.getSlug(), coin);
        this.add(coin.getCoinId(), coin);
    }

    public void add(long key, Coin coin) {
        coins.put(key, coin);
    }

    public void add(List<Coin> coins) {
        if (!DataUtil.isEmpty(coins)) {
            for (Coin coin : coins) {
                add(coin);
            }
        }
    }

    public Coin getCoin(long coinId) {
        if (!hasCoin(coinId)) {
            return null;
        }
        return coins.get(coinId);
    }

    public List<Coin> getCoins() {
        return new ArrayList<>(coins.values());
    }

    public List<Coin> getCoins(List<Long> coinIds) {
        List<Coin> result = new ArrayList<>();
        for (long coinId : coinIds) {
            result.add(getCoin(coinId));
        }
        return result;
    }

    public boolean isExists(Coin in) {
        return map.contains(in.getId());
    }

    public boolean isExists(Quote in) {
        return quoteMap.contains(in.getId());
    }

    public boolean isExpired(long coinId) {
        if (!hasCoin(coinId)) {
            return true;
        }
        Coin coin = getCoin(coinId);
        long coinExpiredTime = Constants.Time.INSTANCE.getCoin();
        return TimeUtil.isExpired(coin.getLastUpdated(), coinExpiredTime);
    }

    public Coin toItem(CoinSource source, CmcCoin in, boolean full) {
        if (in == null) {
            return null;
        }

        long id = DataUtil.getSha512(source.value(), in.getId());
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
        out.setSource(source);
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
        long id = DataUtil.getSha512(currency.name(), coin.getId());
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

    public Coin toItem(CoinSource source, Currency currency, State state, CoinDataSource api) {
        Coin coin = map.get(state.getId());
        if (coin == null) {
            coin = api.getItem(source, currency, state.getId());
        }
        if (coin != null) {
            map.put(coin.getId(), coin);
        }
        return coin;
    }

    public String joinString(String[] values, String separator) {
        return TextUtils.join(separator, values);
    }

    public String joinString(List<String> values, String separator) {
        return TextUtils.join(separator, values);
    }

    public String joinLongToString(List<Long> values, String separator) {
        return TextUtils.join(separator, values);
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

    public List<Coin> filter(List<Coin> source, long lastUpdated) {
        if (DataUtil.isEmpty(source)) {
            return null;
        }
        return null;
    }
}
