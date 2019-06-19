package com.dreampany.lca.data.source.repository;

import com.dreampany.frame.data.source.repository.Repository;
import com.dreampany.frame.misc.Database;
import com.dreampany.frame.misc.Firestore;
import com.dreampany.frame.misc.Remote;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.Room;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.misc.CoinMapper;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.enums.Currency;
import com.dreampany.lca.data.source.api.CoinDataSource;
import com.dreampany.lca.data.source.pref.Pref;
import com.dreampany.network.manager.NetworkManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import hugo.weaving.DebugLog;
import io.reactivex.Maybe;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;
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
    private final CoinDataSource database;
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
                   @Database CoinDataSource database,
                   @Firestore CoinDataSource firestore,
                   @Remote CoinDataSource remote) {
        super(rx, rm);
        this.network = network;
        this.pref = pref;
        this.mapper = mapper;
        this.room = room;
        this.database = database;
        this.firestore = firestore;
        this.remote = remote;
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
        Maybe<List<Coin>> remote = getRemoteItemsIfRx(source, currency, index, limit);
        Maybe<List<Coin>> roomAny = room.getItemsRx(source, currency, index, limit);
        return concatFirstRx(remote, roomAny);
    }

    @Override
    public List<Coin> getItems(CoinSource source, Currency currency, int limit) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, Currency currency, int limit) {
        Maybe<List<Coin>> roomAny = room.getItemsRx(source, currency, limit);
        Maybe<List<Coin>> remoteAny = contactSuccess(remote.getItemsRx(source, currency, limit), coins -> {
            Timber.v("Remote Result %d", coins.size());
            rx.compute(room.putItemsRx(coins)).subscribe(Functions.emptyConsumer(), Functions.emptyConsumer());
        });
        return concatFirstRx(roomAny, remoteAny);
    }

    @Override
    public Coin getItem(CoinSource source, Currency currency, String id) {
        return room.getItem(source, currency, id);
    }

    @Override
    public Maybe<Coin> getItemRx(CoinSource source, Currency currency, String id) {
        Maybe<Coin> firestoreIf = getFirestoreItemIfRx(source, currency, id);
        Maybe<Coin> remoteIf = getRemoteItemIfRx(source, currency, id);
        Maybe<Coin> roomAny = room.getItemRx(source, currency, id);
        return concatSingleLastRx(/*firestoreIf,*/ remoteIf, roomAny);
    }

    @Override
    public List<Coin> getItems(CoinSource source, Currency currency, List<String> ids) {


        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, Currency currency, List<String> ids) {
        Maybe<List<Coin>> firestoreRemote = getFirestoreRemoteItemsIfRx(source, currency, ids);
        Maybe<List<Coin>> firestoreIf = getFirestoreItemsIfRx(source, currency, ids);
        Maybe<List<Coin>> remoteIf = getRemoteItemsIfRx(source, currency, ids);
        Maybe<List<Coin>> roomAny = room.getItemsRx(source, currency, ids);
        return concatLastRx(remoteIf, roomAny);
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
        return room.putItem(coin);
    }

    @Override
    public Maybe<Long> putItemRx(Coin coin) {
        return room.putItemRx(coin);
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
    public Coin getItem(String id) {
        return room.getItem(id);
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
    private Maybe<List<Coin>> getRoomItemsIfRx(CoinSource source, Currency currency, int index, int limit) {
        Maybe<List<Coin>> maybe = Maybe.create(emitter -> {
            List<Coin> result = null;
            if (!room.isEmpty(source, currency, index, limit)) {
                result = room.getItems(source, currency, index, limit);
            }
            if (emitter.isDisposed()) {
                return;
            }
            if (DataUtil.isEmpty(result)) {
                emitter.onError(new EmptyException());
            } else {
                emitter.onSuccess(result);
            }
        });
        return maybe;
    }

    private Maybe<List<Coin>> getRemoteItemsIfRx(CoinSource source, Currency currency, int index, int limit) {
        Maybe<List<Coin>> maybe = mapper.isCoinIndexExpired(source, currency, index)
                ? remote.getItemsRx(source, currency, index, limit)
                : Maybe.empty();
        return maybe.filter(coins -> !DataUtil.isEmpty(coins))
                .doOnSuccess(coins -> {
                    Timber.v("Remote Result %d", coins.size());
                    rx.compute(room.putItemsRx(coins)).subscribe(Functions.emptyConsumer(), Functions.emptyConsumer());
                    //rx.compute(database.putItemsRx(coins)).subscribe(Functions.emptyConsumer(), Functions.emptyConsumer());
                    mapper.updateCoinIndexTime(source, currency, index);
                });
    }

    private Maybe<Coin> getFirestoreItemIfRx(CoinSource source, Currency currency, String coinId) {
        Maybe<Coin> maybe = mapper.isCoinExpired(source, currency, coinId) ? firestore.getItemRx(source, currency, coinId) : Maybe.empty();
        return contactSingleSuccess(maybe, coin -> {
            rx.compute(room.putItemRx(coin)).subscribe(Functions.emptyConsumer(), Functions.emptyConsumer());
            mapper.updateCoinTime(source, currency, coinId);
        });
    }

    private Maybe<Coin> getRemoteItemIfRx(CoinSource source, Currency currency, String coinId) {
        Maybe<Coin> maybe = mapper.isCoinExpired(source, currency, coinId) ? remote.getItemRx(source, currency, coinId) : Maybe.empty();
        return contactSingleSuccess(maybe, coin -> {
            rx.compute(room.putItemRx(coin)).subscribe(Functions.emptyConsumer(), Functions.emptyConsumer());
            //rx.compute(database.putItemRx(coin)).subscribe(Functions.emptyConsumer(), Functions.emptyConsumer());
            mapper.updateCoinTime(source, currency, coinId);
        });
    }

    private Maybe<List<Coin>> getFirestoreRemoteItemsIfRx(CoinSource source, Currency currency, List<String> coinIds) {
        Maybe<List<Coin>> maybe = Maybe.create(emitter -> {
            List<String> ids = new ArrayList<>();
            for (String id : coinIds) {
                if (mapper.isCoinExpired(source, currency, id)) {
                    ids.add(id);
                }
            }
            List<Coin> result = new ArrayList<>();
            if (!DataUtil.isEmpty(ids)) {
                List<Coin> firestoreResult = firestore.getItemsRx(source, currency, ids).blockingGet();
                if (!DataUtil.isEmpty(firestoreResult)) {
                    result.addAll(firestoreResult);
                    for (Coin coin : firestoreResult) {
                        ids.remove(coin.getId());
                    }
                }
            }

            if (!DataUtil.isEmpty(ids)) {
                List<Coin> remoteResult = remote.getItems(source, currency, ids);
                if (!DataUtil.isEmpty(remoteResult)) {
                    result.addAll(remoteResult);
                    //rx.compute(database.putItemsRx(remoteResult)).subscribe(Functions.emptyConsumer(), Functions.emptyConsumer());
                }
            }

            if (emitter.isDisposed()) {
                return;
            }
            if (DataUtil.isEmpty(result)) {
                emitter.onError(new EmptyException());
            } else {
                emitter.onSuccess(result);
            }
        });

        return contactSuccess(maybe, coins -> {
            rx.compute(room.putItemsRx(coins)).subscribe(Functions.emptyConsumer(), Functions.emptyConsumer());
            //rx.compute(database.putItemsRx(coins)).subscribe(Functions.emptyConsumer(), Functions.emptyConsumer());
            for (Coin coin : coins) {
                mapper.updateCoinTime(source, currency, coin.getId());
            }
        });
    }


    private Maybe<List<Coin>> getFirestoreItemsIfRx(CoinSource source, Currency currency, List<String> coinIds) {
        Maybe<List<Coin>> maybe = Maybe.create(emitter -> {
            List<String> ids = new ArrayList<>();
            for (String id : coinIds) {
                if (mapper.isCoinExpired(source, currency, id)) {
                    ids.add(id);
                }
            }
            List<Coin> result = null;
            if (!DataUtil.isEmpty(ids)) {
                result = firestore.getItemsRx(source, currency, ids).blockingGet();
            }
            if (emitter.isDisposed()) {
                return;
            }
            if (DataUtil.isEmpty(result)) {
                emitter.onError(new EmptyException());
            } else {
                emitter.onSuccess(result);
            }
        });

        return contactSuccess(maybe, coins -> {
            rx.compute(room.putItemsRx(coins)).subscribe(Functions.emptyConsumer(), Functions.emptyConsumer());
        });
    }

    private Maybe<List<Coin>> getRemoteItemsIfRx(CoinSource source, Currency currency, List<String> coinIds) {
        Maybe<List<Coin>> maybe = Maybe.create(emitter -> {
            List<String> ids = new ArrayList<>();
            for (String id : coinIds) {
                if (mapper.isCoinExpired(source, currency, id)) {
                    ids.add(id);
                }
            }
            List<Coin> result = null;
            if (!DataUtil.isEmpty(ids)) {
                result = remote.getItems(source, currency, ids);
            }
            if (emitter.isDisposed()) {
                return;
            }
            if (DataUtil.isEmpty(result)) {
                emitter.onError(new EmptyException());
            } else {
                emitter.onSuccess(result);
            }
        });

        return contactSuccess(maybe, coins -> {
            rx.compute(room.putItemsRx(coins)).subscribe(Functions.emptyConsumer(), Functions.emptyConsumer());
            //rx.compute(firestore.putItemsRx(coins)).subscribe(Functions.emptyConsumer(), Functions.emptyConsumer());
            for (Coin coin : coins) {
                mapper.updateCoinTime(source, currency, coin.getId());
            }
        });
    }


    /*
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
