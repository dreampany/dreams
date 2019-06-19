package com.dreampany.lca.data.source.firebase.database;

import com.dreampany.firebase.RxFirebaseDatabase;
import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.enums.Currency;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.Quote;
import com.dreampany.lca.data.source.api.CoinDataSource;
import com.dreampany.lca.misc.Constants;
import com.dreampany.network.manager.NetworkManager;
import com.google.common.collect.Maps;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.MutableTriple;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.inject.Singleton;

import io.reactivex.Maybe;

/**
 * Created by Roman-372 on 5/27/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
public class CoinDatabaseDataSource implements CoinDataSource {

    private final NetworkManager network;
    private final RxFirebaseDatabase database;

    public CoinDatabaseDataSource(NetworkManager network,
                                  RxFirebaseDatabase database) {
        this.network = network;
        this.database = database;
    }

    @Override
    public boolean isEmpty(CoinSource source, Currency currency, int index, int limit) {
        return false;
    }

    @Override
    public List<Coin> getItems(CoinSource source, Currency currency, int index, int limit) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, Currency currency, int index, int limit) {
        return null;
    }

    @Override
    public List<Coin> getItems(CoinSource source, Currency currency, int limit) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, Currency currency, int limit) {
        return null;
    }

    @Override
    public Coin getItem(CoinSource source, Currency currency, String id) {
        return null;
    }

    @Override
    public Maybe<Coin> getItemRx(CoinSource source, Currency currency, String id) {
        return null;
    }

    @Override
    public List<Coin> getItems(CoinSource source, Currency currency, List<String> ids) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, Currency currency, List<String> ids) {
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
        String path = Constants.FirebaseKey.CRYPTO.concat(Constants.Sep.SLASH).concat(Constants.FirebaseKey.COINS);
        String child = String.valueOf(coin.getId());

        Throwable error = database.setItemRx(path, child, coin).blockingGet();
        if (error == null) {
            putQuote(coin);
            return 1;
        }
        return -1;
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
        for (Coin coin : coins) {
            putItem(coin);
        }
        return new ArrayList<>();
/*        String path = Constants.FirebaseKey.CRYPTO.concat(Constants.Sep.SLASH).concat(Constants.FirebaseKey.COINS);
        Map<String, Coin> items = Maps.newHashMap();
        for (Coin coin : coins) {
            items.put(Constants.Sep.HYPHEN.concat(String.valueOf(coin.getId())), coin);
        }
        Throwable error = database.setItemRx(path, items).blockingGet();
        if (error == null) {
            return new ArrayList<>();
        }
        return null;*/
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<Coin> coins) {
        return Maybe.create(emitter -> {
            List<Long> result = putItems(coins);
            if (emitter.isDisposed()) {
                throw new IllegalStateException();
            }
            if (!DataUtil.isEmpty(result)) {
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
    public Coin getItem(String id) {
        return null;
    }

    @Override
    public Maybe<Coin> getItemRx(String id) {
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

    private long putQuote(Coin coin) {
        Quote latest = coin.getLatestQuote();
        if (latest != null) {
            return putQuote(latest);
        }
        return 0;
    }

    private long putQuote(Quote quote) {
        String path = Constants.FirebaseKey.CRYPTO.concat(Constants.Sep.SLASH).concat(Constants.FirebaseKey.QUOTES);
        String child = String.valueOf(quote.getId()).concat(quote.getCurrency().name());

        Throwable error = database.setItemRx(path, child, quote).blockingGet();
        if (error == null) {
            return 1;
        }
        return -1;
    }
}
