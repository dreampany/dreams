package com.dreampany.lca.data.source.firestore;

import com.dreampany.firebase.RxFirestore;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.Currency;
import com.dreampany.lca.data.source.api.CoinDataSource;
import com.dreampany.lca.misc.Constants;
import com.dreampany.network.manager.NetworkManager;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import hugo.weaving.DebugLog;
import io.reactivex.Maybe;

/**
 * Created by Roman-372 on 3/28/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
public class CoinFirestoreDataSource implements CoinDataSource {

    private static final String COINS = Constants.FirestoreKey.COINS;

    private final NetworkManager network;
    private final RxFirestore firestore;

    public CoinFirestoreDataSource(NetworkManager network,
                                   RxFirestore firestore) {
        this.network = network;
        this.firestore = firestore;
    }


    @Override
    public void clear() {

    }

    @Override
    public Coin getItem(CoinSource source, long id, Currency currency) {
        return null;
    }

    @Override
    public Coin getItem(CoinSource source, String symbol, Currency currency) {
        return null;
    }

    @Override
    public Coin getItem(CoinSource source, String symbol, long lastUpdated, Currency currency) {
        return null;
    }

    @Override
    public Maybe<Coin> getItemRx(CoinSource source, String symbol, Currency currency) {
        return null;
    }

    @Override
    public Maybe<Coin> getItemRx(CoinSource source, String symbol, long lastUpdated, Currency currency) {
        Map<String, Object> equalTo = Maps.newHashMap();
        equalTo.put(Constants.CoinKey.SYMBOL, symbol);

        Map<String, Object> lessThanOrEqualTo = Maps.newHashMap();
        lessThanOrEqualTo.put(Constants.CoinKey.LAST_UPDATED, lastUpdated);
        return firestore.getItem(COINS, equalTo, lessThanOrEqualTo, Coin.class);
    }

    @Override
    public List<Coin> getItems(CoinSource source, int index, int limit, Currency currency) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, int index, int limit, Currency currency) {
        return null;
    }

    @Override
    public List<Coin> getItems(CoinSource source, String[] symbols, Currency currency) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, String[] symbols, Currency currency) {
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

    @DebugLog
    @Override
    public long putItem(Coin coin) {
        Throwable error = firestore.setDocument(COINS, coin.getSymbol(), coin).blockingGet();
        if (error == null) {
            return 0;
        }
        return -1;
    }

    @Override
    public Maybe<Long> putItemRx(Coin coin) {
        return Maybe.fromCallable(() -> putItem(coin));
    }

    @Override
    public List<Long> putItems(List<Coin> coins) {
        return null;
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<Coin> coins) {
        return null;
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

/*    public   Maybe<Coin> getFirestoreIf(String symbol, long Class<T> clazz) {
        DocumentReference ref = firestore.collection(collectionPath).document(documentPath);
        return getItem(ref, clazz);
    } */
}
