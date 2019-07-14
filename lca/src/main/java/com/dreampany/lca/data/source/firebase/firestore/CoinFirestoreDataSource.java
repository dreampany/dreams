package com.dreampany.lca.data.source.firebase.firestore;

import com.dreampany.firebase.RxFirebaseFirestore;
import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.enums.Currency;
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
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * Created by Roman-372 on 3/28/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
public class CoinFirestoreDataSource implements CoinDataSource {

    private final NetworkManager network;
    private final RxFirebaseFirestore firestore;

    public CoinFirestoreDataSource(NetworkManager network,
                                   RxFirebaseFirestore firestore) {
        this.network = network;
        this.firestore = firestore;
    }

    @Override
    public boolean isEmpty(CoinSource source, Currency currency, int index, int limit) {
        return false;
    }

    @Override
    public Coin getRandomItem(CoinSource source, Currency currency) {
        return null;
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
    public List<Coin> getItems(CoinSource source, Currency currency) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, Currency currency) {
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
        String collection = Constants.FirebaseKey.CRYPTO;
        TreeSet<MutablePair<String, String>> paths = new TreeSet<>();
        paths.add(MutablePair.of(source.name(), Constants.FirebaseKey.COINS));

        long lastUpdated = TimeUtil.currentTime() - Constants.Time.INSTANCE.getCoin();

        List<MutablePair<String, Object>> equalTo = new ArrayList<>();
        List<MutablePair<String, Object>> greaterThanOrEqualTo = new ArrayList<>();

        equalTo.add(MutablePair.of(Constants.Coin.ID, id));


        greaterThanOrEqualTo.add(MutablePair.of(Constants.Coin.LAST_UPDATED, lastUpdated));

        Maybe<Coin> result = firestore.getItemRx(collection, paths, equalTo, null, greaterThanOrEqualTo, Coin.class);

        result = result.doOnSuccess(new Consumer<Coin>() {

            @Override
            public void accept(Coin coin) throws Exception {

            }
        });

        return result;
    }

    @Override
    public List<Coin> getItems(CoinSource source, Currency currency, List<String> ids) {

        List<Quote> quotes = getQuotes(source, currency, ids);

        if (DataUtil.isEmpty(quotes)) {
            return null;
        }

        String collection = Constants.FirebaseKey.CRYPTO;

        TreeSet<MutablePair<String, String>> paths = new TreeSet<>();
        paths.add(MutablePair.of(source.name(), Constants.FirebaseKey.COINS));

        long lastUpdated = TimeUtil.currentTime() - Constants.Time.INSTANCE.getCoin();

        List<MutablePair<String, Object>> equalTo = new ArrayList<>();
        List<MutablePair<String, Object>> greaterThanOrEqualTo = new ArrayList<>();

        for (Quote quote : quotes) {
            equalTo.add(MutablePair.of(Constants.Coin.ID, quote.getId()));
        }

        greaterThanOrEqualTo.add(MutablePair.of(Constants.Coin.LAST_UPDATED, lastUpdated));

        List<Coin> result = firestore.getItemsRx(collection, paths, equalTo, null, greaterThanOrEqualTo, Coin.class).blockingGet();
        return result;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, Currency currency, List<String> ids) {

        return Maybe.create(emitter -> {
            List<Coin> result = getItems(source, currency, ids);
            if (emitter.isDisposed()) {
                Timber.v("Firestore emitter disposed");
                return;
            }
            if (DataUtil.isEmpty(result)) {
                emitter.onError(new EmptyException());
            } else {
                Timber.v("Firestore Result %d", result.size());
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
        String collection = Constants.FirebaseKey.CRYPTO;
        String document = String.valueOf(coin.getId());
        TreeSet<MutablePair<String, String>> paths = new TreeSet<>();
        paths.add(MutablePair.of(coin.getSource().name(), Constants.FirebaseKey.COINS));

        Throwable error = firestore.setItemRx(collection, paths, document, coin).blockingGet();
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
    public List<Long> putItems(List<? extends Coin> coins) {
        String collection = Constants.FirebaseKey.CRYPTO;
        Map<String, MutableTriple<String, String, Coin>> items = Maps.newHashMap();
        for (Coin coin : coins) {
            items.put(String.valueOf(coin.getId()), MutableTriple.of(coin.getSource().name(), Constants.FirebaseKey.COINS, coin));
        }
        Throwable error = firestore.setItemsRx(collection, items).blockingGet();
        if (error == null) {
            putQuotes(coins);
            return new ArrayList<>();
        }
        return null;
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<? extends Coin> coins) {
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
    public List<Long> delete(List<? extends Coin> coins) {
        return null;
    }

    @Override
    public Maybe<List<Long>> deleteRx(List<? extends Coin> coins) {
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

    /* private api */
    private List<Quote> getQuotes(CoinSource source, Currency currency, List<String> ids) {
        String collection = Constants.FirebaseKey.CRYPTO;

        TreeSet<MutablePair<String, String>> paths = new TreeSet<>();
        paths.add(MutablePair.of(source.name(), Constants.FirebaseKey.QUOTES));

        long lastUpdated = TimeUtil.currentTime() - Constants.Time.INSTANCE.getCoin();

        List<MutablePair<String, Object>> equalTo = new ArrayList<>();
        List<MutablePair<String, Object>> greaterThanOrEqualTo = new ArrayList<>();

        for (String id : ids) {
            equalTo.add(MutablePair.of(Constants.Quote.ID, id));
            equalTo.add(MutablePair.of(Constants.Quote.CURRENCY, currency.name()));
        }

        greaterThanOrEqualTo.add(MutablePair.of(Constants.Coin.LAST_UPDATED, lastUpdated));

        List<Quote> result = firestore.getItemsRx(collection, paths, equalTo, null, greaterThanOrEqualTo, Quote.class).blockingGet();
        return result;
    }


    private long putQuote(Coin coin) {
        Quote latest = coin.getLatestQuote();
        if (latest != null) {
            return putQuote(coin.getSource(), latest);
        }
        return 0;
    }

    private List<Long> putQuotes(List<? extends Coin> coins) {
        String collection = Constants.FirebaseKey.CRYPTO;
        Map<String, MutableTriple<String, String, Quote>> items = Maps.newHashMap();
        for (Coin coin : coins) {
            Quote latest = coin.getLatestQuote();
            if (latest != null) {
                items.put(String.valueOf(latest.getId()).concat(latest.getCurrency().name()), MutableTriple.of(coin.getSource().name(), Constants.FirebaseKey.QUOTES, latest));
            }
        }
        if (!items.isEmpty()) {
            Throwable error = firestore.setItemsRx(collection, items).blockingGet();
            if (error == null) {
                return new ArrayList<>();
            }
        }
        return null;
    }


    private long putQuote(CoinSource source, Quote quote) {
        String collection = Constants.FirebaseKey.CRYPTO;
        String document = String.valueOf(quote.getId()).concat(quote.getCurrency().name());
        TreeSet<MutablePair<String, String>> paths = new TreeSet<>();
        paths.add(MutablePair.of(source.name(), Constants.FirebaseKey.QUOTES));

        Throwable error = firestore.setItemRx(collection, paths, document, quote).blockingGet();
        if (error == null) {
            return 1;
        }
        return -1;
    }

    private void bindQuote(Currency currency, Coin coin) {
        if (coin != null && !coin.hasQuote(currency)) {
            //Quote quote = quoteDao.getItemsWithoutId(coin.getId(), currency.name());
            //coin.addQuote(quote);
        }
    }

}
