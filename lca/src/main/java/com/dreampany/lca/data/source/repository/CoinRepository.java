package com.dreampany.lca.data.source.repository;

import com.dreampany.frame.data.source.repository.Repository;
import com.dreampany.frame.misc.*;
import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.Currency;
import com.dreampany.lca.data.source.api.CoinDataSource;
import com.dreampany.lca.data.source.pref.Pref;
import com.dreampany.lca.misc.Constants;
import com.dreampany.network.manager.NetworkManager;

import io.reactivex.Maybe;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import timber.log.Timber;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Singleton
public class CoinRepository extends Repository<Long, Coin> implements CoinDataSource {

    private final Object guard = new Object();

    private final NetworkManager network;
    private final Pref pref;
    private final CoinDataSource room;
    private final CoinDataSource firestore;
    private final CoinDataSource remote;
    private volatile SyncThread syncThread;

    @Inject
    CoinRepository(RxMapper rx,
                   ResponseMapper rm,
                   NetworkManager network,
                   Pref pref,
                   @Room CoinDataSource room,
                   @Firestore CoinDataSource firestore,
                   @Remote CoinDataSource remote) {
        super(rx, rm);
        this.network = network;
        this.pref = pref;
        this.room = room;
        this.firestore = firestore;
        this.remote = remote;
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
        return room.getCount();
    }

    @Override
    public Maybe<Integer> getCountRx() {
        return room.getCountRx();
    }

    @Override
    public boolean isExists(Coin coin) {
        return room.isExists(coin);
    }

    @Override
    public Maybe<Boolean> isExistsRx(Coin coin) {
        return room.isExistsRx(coin);
    }

    @Override
    public long putItem(Coin coin) {
        return room.putItem(coin);
    }

    @Override
    public Maybe<Long> putItemRx(Coin coin) {
        return room.putItemRx(coin);
                /*.filter(result -> {
                    Timber.v("Room Insertion Result %s", String.valueOf(result));
                    return result != -1;
                })
                .doOnSuccess(result -> rx.compute(firestore.putItemRx(coin)).subscribe());*/
    }

    @Override
    public List<Long> putItems(List<Coin> coins) {
        return room.putItems(coins);
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<Coin> coins) {
        return room.putItemsRx(coins);
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
        return room.getItem(id);
    }

    @Override
    public Maybe<Coin> getItemRx(long id) {
        return getItemRx(id, false);
    }

    @Override
    public List<Coin> getItems() {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx() {
        return getItemsRx(false);
    }

    @Override
    public List<Coin> getItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(int limit) {
        return null;
    }

/*    @Override
    public List<Coin> getListing(CoinSource source) {
        List<Coin> items = remote.getListing(source);
        if (!DataUtil.isEmpty(items)) {
            putItems(items);
            *//*Stream.of(items).forEach(new Consumer<Coin>() {
                @Override
                public void accept(Coin coin) {
                    putItem(coin);
                }
            });*//*
        }
        return items;
    }*/

/*    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source) {
        Maybe<List<Coin>> room = fullRoom(this.room.getItemsRx(source));
        Maybe<List<Coin>> remote = saveRoom(this.remote.getItemsRx(source),
                items -> {
                    rx.compute(putItemsRx(items)).subscribe();
                    pref.commitCoinListingTime();
                });

        long lastTime = pref.getCoinListingTime();
        long period = Constants.Delay.INSTANCE.getCoinListing();
        if (TimeUtil.isExpired(lastTime, period)) {
            return concatFirstRx(remote, room);
        }
        return concatFirstRx(room, remote);
    }

    @Override
    public Maybe<List<Coin>> getListingRx(CoinSource source, int start, int limit) {
        Maybe<List<Coin>> room = fullRoom(this.room.getListingRx(source, start, limit));
        Maybe<List<Coin>> remote = saveRoom(this.remote.getListingRx(source, start, limit),
                items -> {
                    rx.compute(putItemsRx(items)).subscribe();
                    pref.commitCoinListingTime();
                });

        long lastTime = pref.getCoinListingTime();
        long period = Constants.Delay.INSTANCE.getCoinListing();
        if (TimeUtil.isExpired(lastTime, period)) {
            return concatFirstRx(remote, room);
        }
        return concatFirstRx(room, remote);
    }*/

    @Override
    public void clear() {
        room.clear();
        remote.clear();
    }

    @Override
    public Coin getItem(CoinSource source, long id, Currency currency) {
        return room.getItem(source, id, currency);
    }

    @Override
    public Coin getItem(CoinSource source, String symbol, Currency currency) {
        return room.getItem(source, symbol, currency);
    }

    @Override
    public Coin getItem(CoinSource source, String symbol, long lastUpdated, Currency currency) {
        return room.getItem(source, symbol, lastUpdated, currency);
    }

    @Override
    public Maybe<Coin> getItemRx(CoinSource source, String symbol, Currency currency) {
        Maybe<Coin> room = getRoomItemIfRx(source, symbol, currency);
        Maybe<Coin> remote = getRemoteItemIfRx(source, symbol, currency);
        if (needToUpdate(symbol, currency)) {
            return concatSingleFirstRx(remote, room);
        }
        return concatSingleLastRx(remote, room);
    }

    @Override
    public Maybe<Coin> getItemRx(CoinSource source, String symbol, long lastUpdated, Currency currency) {
        Maybe<Coin> roomIf = room.getItemRx(source, symbol, lastUpdated, currency);
        Maybe<Coin> firestoreIf = getFirestoreItemIfRx(source, symbol, lastUpdated, currency);
        Maybe<Coin> remoteIf = getRemoteItemIfRx(source, symbol, lastUpdated, currency);
        Maybe<Coin> roomAny = room.getItemRx(source, symbol, currency);
        return concatSingleFirstRx(roomIf, firestoreIf, remoteIf, roomAny);
    }

    @Override
    public List<Coin> getItems(CoinSource source, int index, int limit, long lastUpdated, Currency currency) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, int index, int limit, long lastUpdated, Currency currency) {
        Maybe<List<Coin>> roomIf = isCoinListingExpired(index, currency) ? Maybe.empty() : room.getItemsRx(source, index, limit, lastUpdated, currency);
        Maybe<List<Coin>> remote = getRemoteItemsIfRx(source, index, limit, lastUpdated, currency);
        Maybe<List<Coin>> roomAny = room.getItemsRx(source, index, limit, lastUpdated, currency);
        return concatFirstRx(roomIf, remote, roomAny);
    }

    @Override
    public List<Coin> getItems(CoinSource source, String[] symbols, Currency currency) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, String[] symbols, Currency currency) {
        Maybe<List<Coin>> room = getRoomItemsIfRx(source, symbols, currency);
        Maybe<List<Coin>> remote = getRemoteItemsIfRx(source, symbols, currency);
        return concatLastRx(remote, room);
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, int[] ids, long lastUpdated, Currency currency) {
        return null;
    }

    /* public api */
    public Maybe<Coin> getItemRx(CoinSource source, String symbol, Currency currency, boolean fresh) {
        Maybe<Coin> room = this.room.getItemRx(source, symbol, currency);
        Maybe<Coin> remote = getWithSingleSave(this.remote.getItemRx(source, symbol, currency));
        return fresh ? concatSingleFirstRx(remote, room) : concatSingleFirstRx(room, remote);
    }

    /* private api */
    private Maybe<Coin> getRoomItemIfRx(CoinSource source, String symbol, Currency currency) {
        return Maybe.create(emitter -> {
            Coin result = room.getItem(source, symbol, currency);
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

    private Maybe<Coin> getRemoteItemIfRx(CoinSource source, String symbol, Currency currency) {
        Maybe<Coin> maybe = Maybe.create(emitter -> {
            Coin coin = remote.getItemRx(source, symbol, currency).blockingGet();
            if (emitter.isDisposed()) {
                throw new IllegalStateException();
            }
            if (coin == null) {
                emitter.onError(new EmptyException());
            } else {
                emitter.onSuccess(coin);
            }
        });

        return maybe
                .filter(coin -> !DataUtil.isEmpty(coin))
                .doOnSuccess(coin -> {
                    rx.compute(putItemRx(coin)).subscribe();
                    updateCoinUpdate(coin.getSymbol(), currency, coin.getLastUpdated());
                });
    }

    private Maybe<Coin> getFirestoreItemIfRx(CoinSource source, String symbol, long lastUpdated, Currency currency) {
        Maybe<Coin> maybe = firestore.getItemRx(source, symbol, lastUpdated, currency);
        return contactSingleSuccess(maybe, coin -> {
            rx.compute(putItemRx(coin)).subscribe();
            updateCoinUpdate(coin.getSymbol(), currency, coin.getLastUpdated());
        });
    }

    private Maybe<Coin> getRemoteItemIfRx(CoinSource source, String symbol, long lastUpdated, Currency currency) {
        Maybe<Coin> maybe = remote.getItemRx(source, symbol, currency);
        return contactSingleSuccess(maybe, coin -> {
            rx.compute(putItemRx(coin)).subscribe();
            rx.compute(firestore.putItemRx(coin)).subscribe();
            updateCoinUpdate(coin.getSymbol(), currency, coin.getLastUpdated());
        });
    }

    private Maybe<List<Coin>> getRoomItemsIfRx(CoinSource source, String[] symbols, Currency currency) {
        return Maybe.create(emitter -> {
            List<Coin> result = room.getItems(source, symbols, currency);
            if (emitter.isDisposed()) {
                throw new IllegalStateException();
            }
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

    private Maybe<List<Coin>> getFirestoreItemsIfRx(CoinSource source, int index, int limit, long lastUpdated, Currency currency) {
        Maybe<List<Coin>> maybe = firestore.getItemsRx(source, index, limit, lastUpdated, currency);
        return contactSuccess(maybe, coins -> {
            rx.compute(putItemsRx(coins)).subscribe();
            updateCoinListing(index, currency);
        });
    }

    private Maybe<List<Coin>> getRemoteItemsIfRx(CoinSource source, int index, int limit, long lastUpdated, Currency currency) {
        Maybe<List<Coin>> maybe = Maybe
                .create(emitter -> {
                    List<Coin> result = remote.getItems(source, index, limit, lastUpdated, currency);
                    if (emitter.isDisposed()) {
                        throw new IllegalStateException();
                    }
                    if (DataUtil.isEmpty(result)) {
                        emitter.onError(new EmptyException());
                    } else {
                        emitter.onSuccess(result);
                    }
                });

        return maybe.filter(coins -> !DataUtil.isEmpty(coins))
                .doOnSuccess(coins -> {
                    rx.compute(putItemsRx(coins)).subscribe();
                    updateCoinListing(index, currency);
                });
    }

    private Maybe<List<Coin>> getRemoteItemsIfRx(CoinSource source, String[] symbols, Currency currency) {
        Maybe<List<Coin>> maybe = Maybe.create(emitter -> {
            List<String> possible = new ArrayList<>();
            for (String symbol : symbols) {
                if (needToUpdate(symbol, currency)) {
                    possible.add(symbol);
                }
            }
            List<Coin> result = null;
            if (!DataUtil.isEmpty(possible)) {
                String[] possibleSymbols = DataUtil.toStringArray(possible);
                result = remote.getItemsRx(source, possibleSymbols, currency).blockingGet();
            }
            if (emitter.isDisposed()) {
                throw new IllegalStateException();
            }
            if (DataUtil.isEmpty(result)) {
                emitter.onError(new EmptyException());
            } else {
                emitter.onSuccess(result);
            }
        });

        return maybe.filter(coins -> !DataUtil.isEmpty(coins))
                .doOnSuccess(coins -> {
                    rx.compute(putItemsRx(coins)).subscribe();
                    for (Coin coin : coins) {
                        updateCoinUpdate(coin.getSymbol(), currency, coin.getLastUpdated());
                    }
                });
    }

    private Maybe<Coin> getItemRx(long id, boolean fresh) {
        Maybe<Coin> local = this.room.getItemRx(id);
        Maybe<Coin> remote = this.remote.getItemRx(id);
        return fresh ? concatSingleFirstRx(remote, local) : concatSingleFirstRx(local, remote);
    }

    private Maybe<List<Coin>> getItemsRx(boolean fresh) {
        Maybe<List<Coin>> local = this.room.getItemsRx();
        Maybe<List<Coin>> remote = contactSuccess(this.remote.getItemsRx(),
                items -> {
                    rx.compute(putItemsRx(items)).subscribe();
                });
        return fresh ? concatFirstRx(remote, local) : concatFirstRx(local, remote);
    }

    private Maybe<Coin> getWithSingleSave(Maybe<Coin> source) {
        return source
                .doOnSuccess(item -> {
                    if (item != null) {
                        rx.compute(putItemRx(item)).subscribe();
                    }
                });
    }

    private boolean isCoinListingExpired(int index, Currency currency) {
        long time = pref.getCoinListingTime(index, currency.name());
        return TimeUtil.isExpired(time, Constants.Time.INSTANCE.getListing());
    }

    private void updateCoinListing(int index, Currency currency) {
        pref.commitCoinListingTime(index, currency.name());
    }

    private boolean needToUpdate(String symbol, Currency currency) {
        long lastTime = pref.getCoinUpdateTime(symbol, currency.name());
        return TimeUtil.isExpired(lastTime, Constants.Time.INSTANCE.getCoin());
    }

    private void updateCoinUpdate(String symbol, Currency currency, long time) {
        pref.commitCoinUpdateTime(symbol, currency.name(), time);
    }

    //syncing process
    private void startRequestThread() {
        synchronized (guard) {
            if (syncThread == null || !syncThread.isRunning()) {
                syncThread = new SyncThread();
                syncThread.start();
            }
            syncThread.notifyRunner();
        }
    }

    private void stopRequestThread() {
        synchronized (guard) {
            if (syncThread != null) {
                syncThread.stop();
            }
        }
    }

    private class SyncThread extends Runner {

        private final long delay = Constants.Time.INSTANCE.getCoin();

        SyncThread() {

        }

        @Override
        protected boolean looping() {
            //1. download listing if expired or not performed
            //2. Take one by one coins from server

/*            long lastTime = pref.getCoinListingTime();
            if (TimeUtil.isExpired(lastTime, delay)) {
                List<Coin> result = getListing(CoinSource.CMC);
                if (!DataUtil.isEmpty(result)) {
                    pref.commitCoinListingTime();
                }
            }*/


            return true;
        }
    }
}
