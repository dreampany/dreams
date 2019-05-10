package com.dreampany.lca.data.source.room;

import com.annimon.stream.Stream;
import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.misc.CoinMapper;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.enums.Currency;
import com.dreampany.lca.data.model.Quote;
import com.dreampany.lca.data.source.api.CoinDataSource;
import com.dreampany.lca.data.source.dao.CoinDao;
import com.dreampany.lca.data.source.dao.QuoteDao;
import io.reactivex.Maybe;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private volatile boolean cacheLoaded;

    public CoinRoomDataSource(CoinMapper mapper,
                              CoinDao dao,
                              QuoteDao quoteDao) {
        this.mapper = mapper;
        this.dao = dao;
        this.quoteDao = quoteDao;
        cacheLoaded = false;
    }

    @Override
    public List<Coin> getItems(CoinSource source, Currency currency, int index, int limit) {
        updateCache();
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
            bindQuote(currency, coin);
        }
        return result;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, Currency currency, int index, int limit) {
        return Maybe.create(emitter -> {
            List<Coin> result = getItems(source, currency, index, limit);
            if (emitter.isDisposed()) {
                return;
            }
            if (DataUtil.isEmpty(result)) {
                emitter.onError(new EmptyException());
            } else {
                emitter.onSuccess(result);
            }
        });
    }

    @Override
    public Coin getItem(CoinSource source, Currency currency, long coinId) {
        if (!mapper.hasCoin(coinId)) {
            Coin room = dao.getItemByCoinId(coinId);
            mapper.add(room);
        }
        Coin cache = mapper.getCoin(coinId);
        if (DataUtil.isEmpty(cache)) {
            return null;
        }
        bindQuote(currency, cache);
        return cache;
    }

    @Override
    public Maybe<Coin> getItemRx(CoinSource source, Currency currency, long coinId) {
        return Maybe.create(emitter -> {
            Coin result = getItem(source, currency, coinId);
            if (emitter.isDisposed()) {
                return;
            }
            if (DataUtil.isEmpty(result)) {
                emitter.onError(new EmptyException());
            } else {
                emitter.onSuccess(result);
            }
        });
    }

    @Override
    public List<Coin> getItems(CoinSource source, Currency currency, List<Long> coinIds) {
        updateCache();
        List<Coin> cache = mapper.getCoins(coinIds);
        if (DataUtil.isEmpty(cache)) {
            return null;
        }
        Collections.sort(cache, (left, right) -> left.getRank() - right.getRank());
        for (Coin coin : cache) {
            bindQuote(currency, coin);
        }
        return cache;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, Currency currency, List<Long> coinIds) {
        return Maybe.create(emitter -> {
            List<Coin> result = getItems(source, currency, coinIds);
            if (emitter.isDisposed()) {
                return;
            }
            if (DataUtil.isEmpty(result)) {
                emitter.onError(new EmptyException());
            } else {
                emitter.onSuccess(result);
            }
        });
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Maybe<Boolean> isEmptyRx() {
        return null;
    }

    @Override
    public int getCount() {
        return dao.getCount();
    }

    @Override
    public Maybe<Integer> getCountRx() {
        return null;
    }

    @Override
    public boolean isExists(Coin coin) {
        return false;
    }

    @Override
    public Maybe<Boolean> isExistsRx(Coin coin) {
        return null;
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
        return Maybe.create(emitter -> {
            long result = putItem(coin);
            if (emitter.isDisposed()) {
                return;
            }
            if (result == -1) {
                emitter.onError(new EmptyException());
            } else {
                emitter.onSuccess(result);
            }
        });
    }

    @Override
    public List<Long> putItems(List<Coin> coins) {
        List<Long> result = new ArrayList<>();
        Stream.of(coins).forEach(coin -> {
            result.add(putItem(coin));
        });
        return result;
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<Coin> coins) {
        return Maybe.create(emitter -> {
            List<Long> result = putItems(coins);
            if (emitter.isDisposed()) {
                return;
            }
            if (DataUtil.isEmpty(result)) {
                emitter.onError(new EmptyException());
            } else {
                emitter.onSuccess(result);
            }
        });
    }

    @Override
    public int delete(Coin coin) {
        return 0;
    }

    @Override
    public Maybe<Integer> deleteRx(Coin coin) {
        return null;
    }

    @Override
    public List<Long> delete(List<Coin> coins) {
        return null;
    }

    @Override
    public Maybe<List<Long>> deleteRx(List<Coin> coins) {
        return null;
    }

    @Override
    public Coin getItem(long id) {
        return dao.getItem(id);
    }

    @Override
    public Maybe<Coin> getItemRx(long id) {
        return null;
    }

    @Override
    public List<Coin> getItems() {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx() {
        return null;
    }

    @Override
    public List<Coin> getItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(int limit) {
        return null;
    }

    /* private */
    private void updateCache() {
        if (!cacheLoaded || !mapper.hasCoins()) {
            List<Coin> room = dao.getItems();
            mapper.add(room);
            cacheLoaded = true;
        }
    }

    private void bindQuote(Currency currency, Coin coin) {
        if (coin != null && !coin.hasQuote(currency)) {
            Quote quote = quoteDao.getItems(coin.getId(), currency.name());
            coin.addQuote(quote);
        }
    }
}
