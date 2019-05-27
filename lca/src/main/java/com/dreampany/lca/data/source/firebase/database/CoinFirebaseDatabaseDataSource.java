package com.dreampany.lca.data.source.firebase.database;

import com.dreampany.firebase.RxFirebaseDatabase;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.enums.Currency;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.source.api.CoinDataSource;
import com.dreampany.lca.misc.Constants;
import com.dreampany.network.manager.NetworkManager;

import org.apache.commons.lang3.tuple.MutablePair;

import java.util.List;
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
public class CoinFirebaseDatabaseDataSource implements CoinDataSource {

    private final NetworkManager network;
    private final RxFirebaseDatabase database;

    public CoinFirebaseDatabaseDataSource(NetworkManager network,
                                          RxFirebaseDatabase database) {
        this.network = network;
        this.database = database;
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
    public Coin getItem(CoinSource source, Currency currency, long id) {
        return null;
    }

    @Override
    public Maybe<Coin> getItemRx(CoinSource source, Currency currency, long id) {
        return null;
    }

    @Override
    public List<Coin> getItems(CoinSource source, Currency currency, List<Long> ids) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, Currency currency, List<Long> ids) {
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
            //putQuote(coin);
            return 1;
        }
        return -1;
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
    public List<Coin> getItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(int limit) {
        return null;
    }
}
