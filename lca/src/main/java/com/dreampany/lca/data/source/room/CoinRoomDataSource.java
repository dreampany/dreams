package com.dreampany.lca.data.source.room;

import com.annimon.stream.Stream;
import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.misc.CoinMapper;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.Currency;
import com.dreampany.lca.data.model.Quote;
import com.dreampany.lca.data.source.api.CoinDataSource;
import com.dreampany.lca.data.source.dao.CoinDao;
import com.dreampany.lca.data.source.dao.QuoteDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Maybe;

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

    public CoinRoomDataSource(CoinMapper mapper,
                              CoinDao dao,
                              QuoteDao quoteDao) {
        this.mapper = mapper;
        this.dao = dao;
        this.quoteDao = quoteDao;
    }

    @Override
    public Coin getItem(CoinSource source, Currency currency, long coinId) {
        return null;
    }

    @Override
    public List<Coin> getItems(CoinSource source, Currency currency, long index, long limit, long lastUpdated) {
        if (!mapper.hasCoins()) {
            List<Coin> room = dao.getItems();
            mapper.add(room);
        }
        List<Coin> cache = mapper.getCoins();
        if (DataUtil.isEmpty(cache)) {
            return null;
        }
        Collections.sort(cache, (left, right) -> left.getRank() - right.getRank());
        List<Coin> result = DataUtil.sub(cache, (int) index, (int) limit);
        if (DataUtil.isEmpty(result)) {
            return null;
        }
        for (Coin coin : result) {
            bindQuote(currency, coin);
        }
        return result;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, Currency currency, long index, long limit, long lastUpdated) {
        return Maybe.create(emitter -> {
            List<Coin> result = getItems(source, currency, index, limit, lastUpdated);
            if (emitter.isDisposed()) {
                throw new IllegalStateException();
            }
            if (DataUtil.isEmpty(result)) {
                emitter.onError(new EmptyException());
            } else {
                emitter.onSuccess(result);
            }
        });
    }

    @Override
    public List<Coin> getItems(CoinSource source, Currency currency, List<Long> coinIds, long lastUpdated) {
        if (!mapper.hasCoins(coinIds)) {
            List<Coin> room = dao.getItems(coinIds, lastUpdated);
            mapper.add(room);
        }
        List<Coin> cache = mapper.getCoins();
        if (DataUtil.isEmpty(cache)) {
            return null;
        }
        Collections.sort(cache, (left, right) -> left.getRank() - right.getRank());
        List<Coin> result = cache;
        if (DataUtil.isEmpty(result)) {
            return null;
        }
        for (Coin coin : result) {
            bindQuote(currency, coin);
        }
        return result;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, Currency currency, List<Long> coinIds, long lastUpdated) {
        return null;
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
        return 0;
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
                throw new IllegalStateException();
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
                throw new IllegalStateException();
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
        return null;
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
    public List<Coin> getItems(long limit) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(long limit) {
        return null;
    }

    /* private */
    private void bindQuote(Currency currency, Coin coin) {
        if (coin != null && !coin.hasQuote(currency)) {
            Quote quote = quoteDao.getItems(coin.getId(), currency.name());
            //coin.clearQuote();
            coin.addQuote(quote);
        }
    }

   /* @Override
    public boolean isEmpty() {
        return getCount() == 0;
    }

    @Override
    public Maybe<Boolean> isEmptyRx() {
        return Maybe.create(emitter -> {
            boolean result = isEmpty();
            if (emitter.isDisposed()) {
                throw new IllegalStateException();
            }
            emitter.onSuccess(result);
        });
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
    public Coin getItem(CoinSource source, long id, Currency currency) {
        if (!mapper.hasCoin(String.valueOf(id))) {
            Coin room = dao.getItem(id);
            mapper.add(String.valueOf(id), room);
        }
        Coin cache = mapper.getCoin(String.valueOf(id));
        if (cache != null) {
            bindQuote(cache, currency);
        }
        return cache;
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
    public Coin getItem(CoinSource source, String symbol, long lastUpdated, Currency currency) {
        Coin coin = getItem(source, symbol, currency);
        if (lastUpdated <= coin.getLastUpdated()) {
            return coin;
        }
        return null;
    }

    @Override
    public Maybe<Coin> getItemRx(CoinSource source, String symbol, Currency currency) {
        return Maybe.create(emitter -> {
            Coin result = getItem(source, symbol, currency);
            if (emitter.isDisposed()) {
                throw new IllegalStateException();
            }
            if (result == null) {
                emitter.onError(new EmptyException());
            } else {
                emitter.onSuccess(result);
            }
        });
    }

    @Override
    public Maybe<Coin> getItemRx(CoinSource source, String symbol, long lastUpdated, Currency currency) {
        return Maybe.create(emitter -> {
            Coin result = getItem(source, symbol, lastUpdated, currency);
            if (emitter.isDisposed()) {
                throw new IllegalStateException();
            }
            if (result == null) {
                emitter.onError(new EmptyException());
            } else {
                emitter.onSuccess(result);
            }
        });
    }

    @Override
    public List<Coin> getItems(CoinSource source, List<String> symbols, Currency currency) {
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
    public Maybe<List<Coin>> getItemsRx(CoinSource source, List<String> symbols, Currency currency) {
        return null;
    }

    @Override
    public List<Coin> getItems(CoinSource source,List<Long> coinIds, long lastUpdated, Currency currency) {
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
    public Maybe<List<Coin>> getItemsRx(CoinSource source, List<Long> coinIds, long lastUpdated, Currency currency) {
        return dao.getItemsRx(coinIds, lastUpdated);
    }

    @Override
    public Coin getItem(long id) {
        if (!mapper.hasCoin(String.valueOf(id))) {
            Coin room = dao.getItem(id);
            mapper.add(room);
        }
        Coin cache = mapper.getCoin(String.valueOf(id));
        if (cache == null) {
            return null;
        }
        //bindQuote(cache, currency);
        return cache;
    }

    @Override
    public Maybe<Coin> getItemRx(long id) {
        return dao.getItemRx(id);
    }

    @Override
    public boolean isExists(Coin coin) {
        //todo bug for exists in ram not in database
        if (mapper.isExists(coin)) {
            return true;
        }
        return dao.getCount(coin.getId()) > 0;
    }

    @Override
    public Maybe<Boolean> isExistsRx(Coin coin) {
        return Maybe.fromCallable(() -> isExists(coin));
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

     private api


    private List<Coin> getItemsIf(List<Flag> items) {
        if (!DataUtil.isEmpty(items)) {
            List<Coin> result = new ArrayList<>(items.size());
            Stream.of(items).forEach(favorite -> result.add(mapper.toItem(favorite, CoinRoomDataSource.this)));
            return result;
        }
        return null;
    }

    private Maybe<List<Coin>> getItemsRx(List<Flag> items) {
        return Flowable.fromIterable(items)
                .map(favorite -> mapper.toItem(favorite, CoinRoomDataSource.this))
                .toList()
                .toMaybe();
    }*/
}
