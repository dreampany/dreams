package com.dreampany.lca.data.misc;

import android.text.TextUtils;

import androidx.core.util.Pair;

import com.dreampany.framework.data.model.State;
import com.dreampany.framework.misc.SmartCache;
import com.dreampany.framework.misc.SmartMap;
import com.dreampany.framework.util.DataUtil;
import com.dreampany.framework.util.NumberUtil;
import com.dreampany.framework.util.TimeUtil;
import com.dreampany.lca.api.cmc.enums.CmcCurrency;
import com.dreampany.lca.api.cmc.model.CmcCoin;
import com.dreampany.lca.api.cmc.model.CmcQuote;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.enums.Currency;
import com.dreampany.lca.data.model.Quote;
import com.dreampany.lca.data.source.api.CoinDataSource;
import com.dreampany.lca.data.source.pref.Pref;
import com.dreampany.lca.misc.CoinAnnote;
import com.dreampany.lca.misc.Constants;
import com.dreampany.lca.misc.QuoteAnnote;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by Hawladar Roman on 5/31/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class CoinMapper {

    private final Pref pref;

    private final SmartMap<String, Coin> map;
    private final SmartCache<String, Coin> cache;

    private final SmartMap<Pair<String, Currency>, Quote> quoteMap;
    private final SmartCache<Pair<String, Currency>, Quote> quoteCache;

    private final Map<String, Coin> coins;
    private final List<Coin> bankCoins;

    @Inject
    CoinMapper(
            Pref pref,
            @CoinAnnote SmartMap<String, Coin> map,
            @CoinAnnote SmartCache<String, Coin> cache,
            @QuoteAnnote SmartMap<Pair<String, Currency>, Quote> quoteMap,
            @QuoteAnnote SmartCache<Pair<String, Currency>, Quote> quoteCache) {
        this.pref = pref;
        this.map = map;
        this.cache = cache;
        this.quoteMap = quoteMap;
        this.quoteCache = quoteCache;
        this.coins = Maps.newConcurrentMap();
        bankCoins = Collections.synchronizedList(new ArrayList<>());
    }

    public boolean hasCoins() {
        return !coins.isEmpty();
    }

    public boolean hasCoin(String coinId) {
        return coins.containsKey(coinId);
    }

    public boolean hasCoins(List<String> coinIds) {
        for (String coinId : coinIds) {
            if (!hasCoin(coinId)) {
                return false;
            }
        }
        return true;
    }

    public void add(Coin coin) {
        this.add(coin.getId(), coin);
        if (!bankCoins.contains(coin)) {
            bankCoins.add(coin);
        }
    }

    public void add(String key, Coin coin) {
        coins.put(key, coin);
    }

    public void add(List<Coin> coins) {
        if (!DataUtil.isEmpty(coins)) {
            for (Coin coin : coins) {
                add(coin);
            }
        }
    }

    public Coin getCoin(String coinId) {
        if (!hasCoin(coinId)) {
            return null;
        }
        return coins.get(coinId);
    }

    public List<Coin> getCoins() {
        return new ArrayList<>(coins.values());
    }

    public List<Coin> getSortedCoins() {
        List<Coin> result = new ArrayList<>(bankCoins);
        Collections.sort(result, (left, right) -> left.getRank() - right.getRank());
        return result;
    }

    public Coin getRandomCoin() {
        int randPosition = NumberUtil.nextRand(bankCoins.size() - 1);
        return bankCoins.get(randPosition);
    }

    public boolean hasCoins(int index, int limit) {
        return (index + limit) <= bankCoins.size();
        //Collections.sort(bankCoins, (left, right) -> left.getRank() - right.getRank());
    }

    public List<Coin> getCoins(List<String> coinIds) {
        List<Coin> result = new ArrayList<>();
        for (String coinId : coinIds) {
            result.add(getCoin(coinId));
        }
        return result;
    }

    public boolean isExists(Coin in) {
        return map.contains(in.getId());
    }

    public boolean isExists(Quote in) {
        return quoteMap.contains(Pair.create(in.getId(), in.getCurrency()));
    }

    public boolean isCoinExpired(CoinSource source, Currency currency, String coinId) {
        long lastTime = pref.getCoinTime(source.name(), currency.name(), coinId);
        return TimeUtil.isExpired(lastTime, Constants.Time.INSTANCE.getCoin());
    }

    public void updateCoinTime(CoinSource source, Currency currency, String coinId, long time) {
        pref.commitCoinTime(source.name(), currency.name(), coinId, time);
    }

    public boolean isCoinIndexExpired(CoinSource source, Currency currency, int index) {
        long time = pref.getCoinIndexTime(source.name(), currency.name(), index);
        return TimeUtil.isExpired(time, Constants.Time.INSTANCE.getListing());
    }

    public void updateCoinIndexTime(CoinSource source, Currency currency, int index) {
        pref.commitCoinIndexTime(source.name(), currency.name(), index);
    }

    public Coin toItem(CoinSource source, CmcCoin in, boolean full) {
        if (in == null) {
            return null;
        }

        String id = String.valueOf(in.getId());
        Coin out = map.get(id);
        if (out == null) {
            out = new Coin();
            if (full) {
                map.put(id, out);
            }
        }
        out.setId(id);
        out.setTime(TimeUtil.currentTime());
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

    private void bindQuote(Coin out, Map<CmcCurrency, CmcQuote> quotes) {
        for (Map.Entry<CmcCurrency, CmcQuote> entry : quotes.entrySet()) {
            Currency currency = toCurrency(entry.getKey());
            Quote quote = toQuote(currency, out, entry.getValue());
            out.addQuote(quote);
        }
    }

    private Currency toCurrency(CmcCurrency currency) {
        return Currency.valueOf(currency.name());
    }

    private Quote toQuote(Currency currency, Coin coin, CmcQuote in) {
        if (in == null) {
            return null;
        }
        Pair<String, Currency> pair = Pair.create(coin.getId(), currency);
        Quote out = quoteMap.get(pair);
        if (out == null) {
            out = new Quote();
            quoteMap.put(pair, out);
        }
        out.setId(coin.getId());
        out.setTime(TimeUtil.currentTime());
        out.setCurrency(currency);
        out.setPrice(in.getPrice());
        out.setDayVolume(in.getDayVolume());
        out.setMarketCap(in.getMarketCap());
        out.setHourChange(in.getHourChange());
        out.setDayChange(in.getDayChange());
        out.setWeekChange(in.getWeekChange());
        out.setLastUpdated(in.getLastUpdatedTime());
        return out;
    }

    public Coin toItem(CoinSource source, Currency currency, State state, CoinDataSource api) {
        Coin coin = map.get(state.getId());
        if (coin == null) {
            coin = api.getItem(state.getId());
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

    public String joinLongToString(List<String> values, String separator) {
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
