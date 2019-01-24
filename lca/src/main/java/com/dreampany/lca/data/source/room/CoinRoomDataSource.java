package com.dreampany.lca.data.source.room;

import com.annimon.stream.Stream;
import com.dreampany.frame.data.model.Flag;
import com.dreampany.frame.data.source.repository.FlagRepository;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.enums.ItemSubtype;
import com.dreampany.lca.data.enums.ItemType;
import com.dreampany.lca.data.misc.CoinMapper;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.Currency;
import com.dreampany.lca.data.model.Quote;
import com.dreampany.lca.data.source.api.CoinDataSource;
import com.dreampany.lca.data.source.dao.CoinDao;
import com.dreampany.lca.data.source.dao.QuoteDao;
import com.google.common.collect.Maps;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.functions.Function;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Hawladar Roman on 30/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Singleton
public class CoinRoomDataSource implements CoinDataSource {

    private final CoinMapper mapper;
    private final CoinDao dao;
    private final QuoteDao quoteDao;
    private final FlagRepository flagRepo;
    private final Map<Coin, Boolean> flags;

    public CoinRoomDataSource(CoinMapper mapper,
                              CoinDao dao,
                              QuoteDao quoteDao,
                              FlagRepository flagRepo) {
        this.mapper = mapper;
        this.dao = dao;
        this.quoteDao = quoteDao;
        this.flagRepo = flagRepo;
        flags = Maps.newConcurrentMap();
    }

    @Override
    public boolean isEmpty() {
        return getCount() == 0;
    }

    @Override
    public Maybe<Boolean> isEmptyRx() {
        return Maybe.fromCallable(this::isEmpty);
    }

    @Override
    public int getCount() {
        return dao.getCount();
    }

    @Override
    public Maybe<Integer> getCountRx() {
        return dao.getCountRx();
    }

    @Override
    public void clear() {

    }

    @Override
    public Coin getItem(CoinSource source, String symbol, Currency currency) {
        if (!mapper.hasCoin(symbol)) {
            Coin room = dao.getItem(symbol);
            mapper.add(room);
        }
        Coin cache = mapper.getCoin(symbol);
        if (cache == null) {
            return null;
        }
        bindQuote(cache, currency);
        return cache;
    }

    @Override
    public Maybe<Coin> getItemRx(CoinSource source, String symbol, Currency currency) {
        return Maybe.fromCallable(() -> {
            Coin coin = mapper.getCoin(symbol);
            if (coin == null) {
                coin = dao.getItem(symbol);
            }
            bindQuote(coin, currency);
            return coin;
        });
    }

    @Override
    public List<Coin> getItems(CoinSource source, int index, int limit, Currency currency) {
        if (!mapper.hasCoins()) {
            List<Coin> room = dao.getItems();
            mapper.add(room);
        }
        List<Coin> cache = mapper.getCoins();
        if (DataUtil.isEmpty(cache)) {
            return null;
        }
        Collections.sort(cache, (left, right) -> left.getRank() - right.getRank());
        List<Coin> result = DataUtil.sub(cache, index, limit);
        if (DataUtil.isEmpty(result)) {
            return null;
        }
        for (Coin coin : result) {
            bindQuote(coin, currency);
        }
        return result;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, int index, int limit, Currency currency) {
        return Maybe.fromCallable(() -> getItems(source, index, limit, currency));
    }

    @Override
    public List<Coin> getItems(CoinSource source, String[] symbols, Currency currency) {
        if (!mapper.hasCoins(symbols)) {
            List<Coin> room = dao.getItems(symbols);
            mapper.add(room);
        }
        List<Coin> cache = mapper.getCoins(symbols);
        if (DataUtil.isEmpty(cache)) {
            return null;
        }
        Collections.sort(cache, (left, right) -> left.getRank() - right.getRank());
        if (DataUtil.isEmpty(cache)) {
            return null;
        }
        for (Coin coin : cache) {
            bindQuote(coin, currency);
        }
        return cache;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, String[] symbols, Currency currency) {
        return null;
    }

    @Override
    public Coin getItem(long id) {
        return dao.getItem(id);
    }

    @Override
    public Maybe<Coin> getItemRx(long id) {
        return dao.getItemRx(id);
    }

    @Override
    public boolean isFlagged(Coin coin) {
        if (flags.containsKey(coin)) {
            return flags.get(coin);
        }
        Flag flag = flagRepo.getItem(coin.getId(), ItemType.COIN.name(), ItemSubtype.DEFAULT.name());
        return flagRepo.isExists(flag);
    }

    @Override
    public Maybe<Boolean> isFlaggedRx(Coin coin) {
        if (flags.containsKey(coin)) {
            return Maybe.fromCallable(() -> flags.get(coin));
        }
        Maybe<Flag> single = flagRepo.getItemRx(coin.getId(), ItemType.COIN.name(), ItemSubtype.DEFAULT.name());
        return single.map(flagRepo::isExists);
    }

    @Override
    public long putFlag(Coin coin) {
        Flag flag = flagRepo.getItem(coin.getId(), ItemType.COIN.name(), ItemSubtype.DEFAULT.name());
        return flagRepo.putItem(flag);
    }

    @Override
    public Maybe<Long> putFlagRx(Coin coin) {
        Maybe<Flag> single = flagRepo.getItemRx(coin.getId(), ItemType.COIN.name(), ItemSubtype.DEFAULT.name());
        return single.map(flagRepo::putItem);
    }

    @Override
    public List<Long> putFlags(List<Coin> coins) {
        List<Long> result = new ArrayList<>(coins.size());
        for (Coin coin : coins) {
            result.add(putFlag(coin));
        }
        return result;
    }

    @Override
    public Maybe<List<Long>> putFlagsRx(List<Coin> coins) {
        return null;
    }

    @Override
    public boolean toggleFlag(Coin coin) {
        Flag flag = flagRepo.getItem(coin.getId(), ItemType.COIN.name(), ItemSubtype.DEFAULT.name());
        boolean flagged = flagRepo.toggle(flag);
        flags.put(coin, flagged);
        return flagged;
    }

    @Override
    public Maybe<Boolean> toggleFlagRx(Coin coin) {
        Maybe<Flag> maybe = flagRepo.getItemRx(coin.getId(), ItemType.COIN.name(), ItemSubtype.DEFAULT.name());
        return maybe.map(flag -> {
            boolean flagged = flagRepo.toggle(flag);
            flags.put(coin, flagged);
            return flagged;
        });
    }

    @Override
    public List<Coin> getFlags() {
        return getItems(flagRepo.getItems());
    }

    @Override
    public Maybe<List<Coin>> getFlagsRx() {
        return flagRepo.getItemsRx()
                .flatMap((Function<List<Flag>, MaybeSource<List<Coin>>>) this::getItemsRx);
    }

    @Override
    public List<Coin> getFlags(int limit) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getFlagsRx(int limit) {
        return null;
    }

    @Override
    public boolean isExists(Coin coin) {
        //todo bug for exists in ram not in database
/*        if (mapper.isExists(coin)) {
            return true;
        }*/
        return dao.getCount(coin.getId()) > 0;
    }

    @Override
    public Maybe<Boolean> isExistsRx(Coin coin) {
        return Maybe.fromCallable(() -> isExists(coin));
    }

    @Override
    public long putItem(Coin coin) {
        mapper.add(coin); //adding mapper to reuse
        if (coin.hasQuote()) {
            quoteDao.insertOrReplace(coin.getQuotesAsList());
        }
        return dao.insertOrReplace(coin);
    }

    @Override
    public Maybe<Long> putItemRx(Coin coin) {
        return Maybe.fromCallable(() -> {
            long result = putItem(coin);
            return result;
        });
    }

    @Override
    public List<Long> putItems(List<Coin> coins) {
        List<Long> result = new ArrayList<>();
        Stream.of(coins).forEach(coin -> {
            result.add(putItem(coin));
        });
        //int count = getCount();
        //Timber.v("Input Coins %d Inserted Coins %d total %d", coins.size(), result.size(), count);
        return result;
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<Coin> coins) {
        return Maybe.fromCallable(() -> putItems(coins));
/*        return Single.fromCallable(new Callable<List<Long>>() {
            @Override
            public List<Long> call() throws Exception {
                List<Long> result = new ArrayList<>();
                for (Coin coin : coins) {
                    long res = putItem(coin);
                    result.add(res);
                    Timber.v("Result %d", res);
                }
                return result;
            }
        });*/
    }

    @Override
    public List<Coin> getItems() {
        return dao.getItems();
    }

    @Override
    public Maybe<List<Coin>> getItemsRx() {
        return dao.getItemsRx();
    }

    @Override
    public List<Coin> getItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(int limit) {
        return null;
    }

    /**
     * private api
     */
    private void bindQuote(Coin coin, Currency currency) {
        if (coin != null && !coin.hasQuote(currency)) {
            Quote quote = quoteDao.getItems(coin.getId(), currency.name());
            //coin.clearQuote();
            coin.addQuote(quote);
        }
    }

    private List<Coin> getItems(List<Flag> items) {
        if (!DataUtil.isEmpty(items)) {
            List<Coin> result = new ArrayList<>(items.size());
            Stream.of(items).forEach(flag -> result.add(mapper.toItem(flag, CoinRoomDataSource.this)));
            return result;
        }
        return null;
    }

    private Maybe<List<Coin>> getItemsRx(List<Flag> items) {
        return Flowable.fromIterable(items)
                .map(flag -> mapper.toItem(flag, CoinRoomDataSource.this))
                .toList()
                .toMaybe();
    }
}
