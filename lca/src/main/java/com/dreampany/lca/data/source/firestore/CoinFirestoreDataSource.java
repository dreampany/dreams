package com.dreampany.lca.data.source.firestore;

import com.dreampany.firebase.RxFirestore;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.Currency;
import com.dreampany.lca.data.source.api.CoinDataSource;
import com.dreampany.lca.misc.Constants;
import com.dreampany.network.manager.NetworkManager;

import java.util.List;

import javax.inject.Singleton;

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
    public Coin getItem(CoinSource source, Currency currency, long coinId) {
        return null;
    }

    @Override
    public List<Coin> getItems(CoinSource source, Currency currency, long index, long limit, long lastUpdated) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, Currency currency, long index, long limit, long lastUpdated) {
        return null;
    }

    @Override
    public List<Coin> getItems(CoinSource source, Currency currency, List<Long> coinIds, long lastUpdated) {
        return null;
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
        return 0;
    }

    @Override
    public Maybe<Long> putItemRx(Coin coin) {
        return null;
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
    public List<Coin> getItems(long limit) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(long limit) {
        return null;
    }

/*    Map<String, Object> equalTo = Maps.newHashMap();
        equalTo.put(Constants.CoinKey.SYMBOL, symbol);

    Map<String, Object> greaterThanOrEqualTo = Maps.newHashMap();
        greaterThanOrEqualTo.put(Constants.CoinKey.LAST_UPDATED, lastUpdated);
    Maybe<Coin> result = firestore.getItemRx(COINS, equalTo, null, greaterThanOrEqualTo, Coin.class);

    result = result.doOnSuccess(new Consumer<Coin>() {
        @DebugLog
        @Override
        public void accept(Coin coin) throws Exception {

        }
    });

        return result;
    Throwable error = firestore.setDocument(COINS, coin.getSymbol(), coin).blockingGet();
        if (error == null) {
        return 0;
    }
        return -1;*/


}
