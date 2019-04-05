package com.dreampany.lca.data.source.repository;

import com.dreampany.frame.data.source.repository.Repository;
import com.dreampany.frame.misc.Firestore;
import com.dreampany.frame.misc.Remote;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.Room;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.misc.CoinMapper;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.Currency;
import com.dreampany.lca.data.source.api.CoinDataSource;
import com.dreampany.lca.data.source.pref.Pref;
import com.dreampany.lca.misc.Constants;
import com.dreampany.network.manager.NetworkManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import hugo.weaving.DebugLog;
import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

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
    private CoinMapper mapper;
    private final CoinDataSource room;
    private final CoinDataSource firestore;
    private final CoinDataSource remote;
    //private volatile SyncThread syncThread;

    @Inject
    CoinRepository(RxMapper rx,
                   ResponseMapper rm,
                   NetworkManager network,
                   Pref pref,
                   CoinMapper mapper,
                   @Room CoinDataSource room,
                   @Firestore CoinDataSource firestore,
                   @Remote CoinDataSource remote) {
        super(rx, rm);
        this.network = network;
        this.pref = pref;
        this.mapper = mapper;
        this.room = room;
        this.firestore = firestore;
        this.remote = remote;
    }

    @Override
    public List<Coin> getItems(CoinSource source, Currency currency, int index, int limit) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, Currency currency, int index, int limit) {
        return null;
    }

/*    @Override
    public List<Coin> getItems(CoinSource source, Currency currency, int index, int limit, long lastUpdated) {


        return null;
    }

    @DebugLog
    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, Currency currency, int index, int limit, long lastUpdated) {
        Maybe<List<Coin>> remote = isListingExpired(currency, index) ?
                getRemoteItemsIfRx(source, currency, index, limit) :
                Maybe.empty();
        Maybe<List<Coin>> roomIf = room.getItemsRx(source, currency, index, limit);
        return concatLastRx(remote, roomIf);
    }*/

    @Override
    public Coin getItem(CoinSource source, Currency currency, long coinId) {
        return room.getItem(source, currency, coinId);
    }

    @Override
    public Maybe<Coin> getItemRx(CoinSource source, Currency currency, long coinId) {
        return null;
    }

/*    @Override
    public Coin getItem(CoinSource source, Currency currency, long coinId, long lastUpdated) {
        return null;
    }

    @Override
    public Maybe<Coin> getItemRx(CoinSource source, Currency currency, long coinId, long lastUpdated) {
        Maybe<Coin> remote = getRemoteItemIfRx(source, currency, coinId);
        Maybe<Coin> roomAny = room.getItemRx(source, currency, coinId);
        return concatSingleLastRx(remote, roomAny);
        Maybe<Coin> roomIf = room.getItemRx(source, currency, coinId, lastUpdated);
        Maybe<Coin> remoteIf = getRemoteItemIfRx(source, currency, coinId);
        Maybe<Coin> roomAny = room.getItemRx(source, currency, coinId);
        return concatSingleFirstRx(roomIf, remoteIf, roomAny);
    }*/

    @Override
    public List<Coin> getItems(CoinSource source, Currency currency, List<Long> coinIds) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, Currency currency, List<Long> coinIds) {
        return null;
    }

/*    @Override
    public List<Coin> getItems(CoinSource source, Currency currency, List<Long> coinIds, long lastUpdated) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, Currency currency, List<Long> coinIds, long lastUpdated) {
        Maybe<List<Coin>> roomIf = room.getItemsRx(source, currency, coinIds, lastUpdated);
        Maybe<List<Coin>> remoteIf = getRemoteItemsIfRx(source, currency, coinIds);
        Maybe<List<Coin>> roomAny = room.getItemsRx(source, currency, coinIds);
        return Maybe.create(new MaybeOnSubscribe<List<Coin>>() {
            @Override
            public void subscribe(MaybeEmitter<List<Coin>> emitter) throws Exception {
                List<Coin> result = new ArrayList<>();

            }
        });
        //return concatFirstRx(roomIf, remoteIf, roomAny);
    }*/

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
        return room.getCount();
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
        return room.putItems(coins);
    }

    @DebugLog
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
    private boolean isListingExpired(Currency currency, int index) {
        long time = pref.getCoinListingTime(currency.name(), index);
        return TimeUtil.isExpired(time, Constants.Time.INSTANCE.getListing());
    }

    private void updateListing(Currency currency, int index) {
        pref.commitCoinListingTime(currency.name(), index);
    }

/*    private boolean isCoinExpired(Currency currency, long coinId) {
        long lastTime = pref.getCoinUpdateTime(currency.name(), coinId);
        return TimeUtil.isExpired(lastTime, Constants.Time.INSTANCE.getCoin());
    }

    private void updateCoin(Currency currency, long coinId, long time) {
        pref.commitCoinUpdateTime(currency.name(), coinId, time);
    }*/

    @DebugLog
    private Maybe<List<Coin>> getRemoteItemsIfRx(CoinSource source, Currency currency, int index, int limit) {
        Maybe<List<Coin>> maybe = remote.getItemsRx(source, currency, index, limit);
        return maybe.filter(coins -> !DataUtil.isEmpty(coins))
                .doOnSuccess(coins -> {
                    Timber.v("Remote Result %d", coins.size());
                    rx.compute(putItemsRx(coins)).subscribe();
                    updateListing(currency, index);
                });
    }

    private Maybe<Coin> getRemoteItemIfRx(CoinSource source, Currency currency, long coinId) {
        Maybe<Coin> maybe = mapper.isExpired(coinId) ? remote.getItemRx(source, currency, coinId) : Maybe.empty();
        return contactSingleSuccess(maybe, coin -> {
            rx.compute(putItemRx(coin)).subscribe();
        });
    }

    private Maybe<List<Coin>> getRemoteItemsIfRx(CoinSource source, Currency currency, List<Long> coinIds) {
        return contactSuccess(remote.getItemsRx(source, currency, coinIds), coins -> rx.compute(putItemsRx(coins)).subscribe());
    }

    /*@Override
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
                .filter(result -> {
                    Timber.v("Room Insertion Result %s", String.valueOf(result));
                    return result != -1;
                })
                .doOnSuccess(result -> rx.compute(firestore.putItemRx(coin)).subscribe());
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

    @Override
    public List<Coin> getListing(CoinSource source) {
        List<Coin> items = remote.getListing(source);
        if (!DataUtil.isEmpty(items)) {
            putItems(items);
            Stream.of(items).forEach(new Consumer<Coin>() {
                @Override
                public void accept(Coin coin) {
                    putItem(coin);
                }
            });
        }
        return items;
    }

    @Override
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
    }

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
        if (isCoinExpired(symbol, currency)) {
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
    public List<Coin> getItems(CoinSource source, List<String> symbols, Currency currency) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, List<String> symbols, Currency currency) {
        Maybe<List<Coin>> roomIf = room.getItemsRx(source, symbols, currency);
        Maybe<List<Coin>> remote = getRemoteItemsIfRx(source, symbols, currency);
        return concatLastRx(remote, roomIf);
    }

    @Override
    public List<Coin> getItems(CoinSource source, List<Long> coinIds, long lastUpdated, Currency currency) {
        return null;
    }

    *
     * first peek from room of possible coins with lastUpdated
     * then from firestore
     * then fillup from remotes
     *
     * @param source
     * @param coinIds
     * @param lastUpdated
     * @param currency
     * @return

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, List<Long> coinIds, long lastUpdated, Currency currency) {
        Maybe<List<Coin>> roomIf = room.getItemsRx(source, coinIds, lastUpdated, currency);
        Maybe<List<Coin>> remote = getRemoteItemsIfRx(source, coinIds, lastUpdated, currency);
        return concatLastRx(remote, roomIf);
    }

     public api
    public Maybe<Coin> getItemRx(CoinSource source, String symbol, Currency currency, boolean fresh) {
        Maybe<Coin> roomIf = room.getItemRx(source, symbol, currency);
        Maybe<Coin> remote = getWithSingleSave(this.remote.getItemRx(source, symbol, currency));
        return fresh ? concatSingleFirstRx(remote, roomIf) : concatSingleFirstRx(roomIf, remote);
    }

     private api
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
                    updateCoin(coin.getSymbol(), currency, coin.getLastUpdated());
                });
    }

    private Maybe<Coin> getFirestoreItemIfRx(CoinSource source, String symbol, long lastUpdated, Currency currency) {
        Maybe<Coin> maybe = firestore.getItemRx(source, symbol, lastUpdated, currency);
        return contactSingleSuccess(maybe, coin -> {
            rx.compute(putItemRx(coin)).subscribe();
            updateCoin(coin.getSymbol(), currency, coin.getLastUpdated());
        });
    }

    private Maybe<Coin> getRemoteItemIfRx(CoinSource source, String symbol, long lastUpdated, Currency currency) {
        Maybe<Coin> maybe = remote.getItemRx(source, symbol, currency);
        return contactSingleSuccess(maybe, coin -> {
            rx.compute(putItemRx(coin)).subscribe();
            rx.compute(firestore.putItemRx(coin)).subscribe();
            updateCoin(coin.getSymbol(), currency, coin.getLastUpdated());
        });
    }

    private Maybe<List<Coin>> getFirestoreItemsIfRx(CoinSource source, int index, int limit, long lastUpdated, Currency currency) {
        Maybe<List<Coin>> maybe = firestore.getItemsRx(source, index, limit, lastUpdated, currency);
        return contactSuccess(maybe, coins -> {
            rx.compute(putItemsRx(coins)).subscribe();
            updateCoinListing(index, currency);
        });
    }



    private Maybe<List<Coin>> getRemoteItemsIfRx(CoinSource source, List<String> symbols, Currency currency) {
        Maybe<List<Coin>> maybe = Maybe.create(emitter -> {
            List<String> possibleSymbols = new ArrayList<>();
            for (String symbol : symbols) {
                if (isCoinExpired(symbol, currency)) {
                    possibleSymbols.add(symbol);
                }
            }
            List<Coin> result = null;
            if (!DataUtil.isEmpty(possibleSymbols)) {
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
                        updateCoin(coin.getSymbol(), currency, coin.getLastUpdated());
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

    private boolean isCoinExpired(String symbol, Currency currency) {
        long lastTime = pref.getCoinUpdateTime(symbol, currency.name());
        return TimeUtil.isExpired(lastTime, Constants.Time.INSTANCE.getCoin());
    }



    private void updateCoin(long coinId, Currency currency, long time) {
        pref.commitCoinUpdateTime(coinId, currency.name(), time);
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

            long lastTime = pref.getCoinListingTime();
            if (TimeUtil.isExpired(lastTime, delay)) {
                List<Coin> result = getListing(CoinSource.CMC);
                if (!DataUtil.isEmpty(result)) {
                    pref.commitCoinListingTime();
                }
            }


            return true;
        }
    }*/
}
