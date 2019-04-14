package com.dreampany.lca.data.source.firestore;

import com.dreampany.firebase.RxFirestore;
import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.Currency;
import com.dreampany.lca.data.model.Quote;
import com.dreampany.lca.data.source.api.CoinDataSource;
import com.dreampany.lca.misc.Constants;
import com.dreampany.network.manager.NetworkManager;
import com.google.common.collect.Maps;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.MutableTriple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.inject.Singleton;

import androidx.core.util.Pair;
import hugo.weaving.DebugLog;
import io.reactivex.Maybe;
import io.reactivex.functions.Consumer;

/**
 * Created by Roman-372 on 3/28/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
public class CoinFirestoreDataSource implements CoinDataSource {

    private final NetworkManager network;
    private final RxFirestore firestore;

    public CoinFirestoreDataSource(NetworkManager network,
                                   RxFirestore firestore) {
        this.network = network;
        this.firestore = firestore;
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
    public Coin getItem(CoinSource source, Currency currency, long coinId) {
        return null;
    }


    @Override
    public Maybe<Coin> getItemRx(CoinSource source, Currency currency, long coinId) {
        String collection = Constants.FirestoreKey.CRYPTO;
        TreeSet<MutablePair<String, String>> paths = new TreeSet<>();
        paths.add(MutablePair.of(source.name(), Constants.FirestoreKey.COINS));

        long lastUpdated = TimeUtil.currentTime() - Constants.Time.INSTANCE.getCoin();

        List<MutablePair<String, Object>> equalTo = new ArrayList<>();
        List<MutablePair<String, Object>> greaterThanOrEqualTo = new ArrayList<>();

        equalTo.add(MutablePair.of(Constants.CoinKey.COIN_ID, coinId));


        greaterThanOrEqualTo.add(MutablePair.of(Constants.CoinKey.LAST_UPDATED, lastUpdated));

        Maybe<Coin> result = firestore.getItemRx(collection, paths, equalTo, null, greaterThanOrEqualTo, Coin.class);

        result = result.doOnSuccess(new Consumer<Coin>() {
            @DebugLog
            @Override
            public void accept(Coin coin) throws Exception {

            }
        });

        return result;
    }

    @Override
    public List<Coin> getItems(CoinSource source, Currency currency, List<Long> coinIds) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, Currency currency, List<Long> coinIds) {
        String collection = Constants.FirestoreKey.CRYPTO;
        TreeSet<MutablePair<String, String>> paths = new TreeSet<>();
        paths.add(MutablePair.of(source.name(), Constants.FirestoreKey.COINS));

        long lastUpdated = TimeUtil.currentTime() - Constants.Time.INSTANCE.getCoin();

        List<MutablePair<String, Object>> equalTo = new ArrayList<>();
        List<MutablePair<String, Object>> greaterThanOrEqualTo = new ArrayList<>();

        for (long coinId : coinIds) {
            equalTo.add(MutablePair.of(Constants.CoinKey.COIN_ID, coinId));
        }

        greaterThanOrEqualTo.add(MutablePair.of(Constants.CoinKey.LAST_UPDATED, lastUpdated));

        Maybe<List<Coin>> result = firestore.getItemsRx(collection, paths, equalTo, null, greaterThanOrEqualTo, Coin.class);

        result = result.doOnSuccess(new Consumer<List<Coin>>() {
            @DebugLog
            @Override
            public void accept(List<Coin> coins) throws Exception {

            }
        });

        return result;
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

    @DebugLog
    @Override
    public long putItem(Coin coin) {
        String collection = Constants.FirestoreKey.CRYPTO;
        String document = String.valueOf(coin.getCoinId());
        TreeSet<MutablePair<String, String>> paths = new TreeSet<>();
        paths.add(MutablePair.of(coin.getSource().name(), Constants.FirestoreKey.COINS));

        Throwable error = firestore.setItemRx(collection, paths, document, coin).blockingGet();
        if (error == null) {
            return 0;
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
        String collection = Constants.FirestoreKey.CRYPTO;
        Map<String, MutableTriple<String, String, Coin>> items = Maps.newHashMap();
        for (Coin coin : coins) {
            items.put(String.valueOf(coin.getCoinId()), MutableTriple.of(coin.getSource().name(), Constants.FirestoreKey.COINS, coin));
        }
        Throwable error = firestore.setItemsRx(collection, items).blockingGet();
        if (error == null) {
            return new ArrayList<>();
        }
        return null;
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
    public List<Coin> getItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(int limit) {
        return null;
    }

    /* private api */
    private void bindQuote(Currency currency, Coin coin) {
        if (coin != null && !coin.hasQuote(currency)) {
            //Quote quote = quoteDao.getItems(coin.getId(), currency.name());
            //coin.addQuote(quote);
        }
    }
}
