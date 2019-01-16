package com.dreampany.lca.data.source.repository;

import android.annotation.SuppressLint;

import com.dreampany.frame.data.source.repository.Repository;
import com.dreampany.frame.misc.Remote;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.Room;
import com.dreampany.frame.misc.Runner;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.Currency;
import com.dreampany.lca.data.source.api.CoinDataSource;
import com.dreampany.lca.data.source.pref.Pref;
import com.dreampany.lca.misc.Constants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Maybe;
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

    private final Pref pref;
    private final CoinDataSource room;
    private final CoinDataSource remote;
    private CoinSource coinSource;
    private volatile SyncThread syncThread;

    @SuppressLint("CheckResult")
    @Inject
    CoinRepository(RxMapper rx,
                   ResponseMapper rm,
                   Pref pref,
                   @Room CoinDataSource room,
                   @Remote CoinDataSource remote) {
        super(rx, rm);
        this.pref = pref;
        this.room = room;
        this.remote = remote;
        coinSource = CoinSource.CMC;
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
    public Coin getItem(long id) {
        return null;
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
            /*Stream.of(items).forEach(new Consumer<Coin>() {
                @Override
                public void accept(Coin coin) {
                    putItem(coin);
                }
            });*/
        }
        return items;
    }

    @Override
    public Maybe<List<Coin>> getListingRx(CoinSource source) {
        Maybe<List<Coin>> room = fullRoom(this.room.getListingRx(source));
        Maybe<List<Coin>> remote = saveRoom(this.remote.getListingRx(source),
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
    public Maybe<List<Coin>> getListingRx(CoinSource source, int start, int limit, String[] currencies) {
        Maybe<List<Coin>> room = fullRoom(this.room.getListingRx(source, start, limit, currencies));
        Maybe<List<Coin>> remote = saveRoom(this.remote.getListingRx(source, start, limit, currencies),
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
    public List<Coin> getListing(CoinSource source, int limit) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getListingRx(CoinSource source, int limit) {
        return null;
    }

    @Override
    public List<Coin> getItems(CoinSource source, int start) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, int start) {
        return null;
    }

    @Override
    public List<Coin> getItems(CoinSource source, int start, int limit) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, int start, int limit) {
        return getItemsRx(source, start, limit, false);
    }

    @Override
    public Coin getItemByCoinId(long coinId) {
        return null;
    }

    @Override
    public Maybe<Coin> getItemByCoinIdRx(long coinId) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsByCoinIdsRx(List<Long> coinIds) {
        return remote.getItemsByCoinIdsRx(coinIds);
    }

    @Override
    public Coin getItemBySymbol(String symbol) {
        return null;
    }

    @Override
    public Maybe<Coin> getItemBySymbolRx(String symbol) {
        return null;
    }

    @Override
    public boolean isFlagged(Coin coin) {
        return room.isFlagged(coin);
    }

    @Override
    public Maybe<Boolean> isFlaggedRx(Coin coin) {
        return room.isExistsRx(coin);
    }

    @Override
    public long putFlag(Coin coin) {
        return room.putFlag(coin);
    }

    @Override
    public Maybe<Long> putFlagRx(Coin coin) {
        return room.putFlagRx(coin);
    }

    @Override
    public List<Long> putFlags(List<Coin> coins) {
        return room.putFlags(coins);
    }

    @Override
    public Maybe<List<Long>> putFlagsRx(List<Coin> coins) {
        return room.putFlagsRx(coins);
    }

    @Override
    public boolean toggleFlag(Coin coin) {
        return room.toggleFlag(coin);
    }

    @Override
    public Maybe<Boolean> toggleFlagRx(Coin coin) {
        return null;
    }

    @Override
    public List<Coin> getFlags() {
        return room.getFlags();
    }

    @Override
    public Maybe<List<Coin>> getFlagsRx() {
        return room.getFlagsRx();
    }

    @Override
    public List<Coin> getFlags(int limit) {
        return room.getFlags(limit);
    }

    @Override
    public Maybe<List<Coin>> getFlagsRx(int limit) {
        return room.getFlagsRx(limit);
    }

    //fresh api
    public Maybe<Coin> getItemRx(long id, boolean fresh) {
        Maybe<Coin> local = this.room.getItemRx(id);
        Maybe<Coin> remote = this.remote.getItemRx(id);
        return fresh ? concatSingleFirstRx(remote, local) : concatSingleFirstRx(local, remote);
    }

    public Maybe<Coin> getItemByCoinIdRx(long coinId, boolean fresh) {
        Maybe<Coin> local = this.room.getItemByCoinIdRx(coinId);
        Maybe<Coin> remote = getWithSingleSave(this.remote.getItemByCoinIdRx(coinId));
        return fresh ? concatSingleFirstRx(remote, local) : concatSingleFirstRx(local, remote);
    }

    public Maybe<List<Coin>> getItemsRx(boolean fresh) {
        Maybe<List<Coin>> local = this.room.getItemsRx();
        Maybe<List<Coin>> remote = saveRoom(this.remote.getItemsRx(),
                items -> {
                    rx.compute(putItemsRx(items)).subscribe();
                });
        return fresh ? concatFirstRx(remote, local) : concatFirstRx(local, remote);
    }

    public Maybe<List<Coin>> getItemsRx(CoinSource source, int start, int limit, boolean fresh) {
        Maybe<List<Coin>> local = this.room.getItemsRx(source, start, limit);
        Maybe<List<Coin>> remote = saveRoom(this.remote.getItemsRx(source, start, limit),
                items -> rx.compute(putItemsRx(items)).subscribe());
        return fresh ? concatFirstRx(remote, local) : concatFirstRx(local, remote);
    }

    private Maybe<List<Coin>> fullRoom(Maybe<List<Coin>> room) {
        return Maybe.fromCallable(() -> {
            if (!isEmpty()) {
                return room.blockingGet();
            }
            return null;
        });
    }

    public Maybe<List<Coin>> getRemoteListingIfRx(CoinSource source) {
        long lastTime = pref.getCoinListingTime();
        if (TimeUtil.isExpired(lastTime, Constants.Delay.INSTANCE.getCoinListing())) {
            return this.remote.getListingRx(source)
                    .filter(items -> !(DataUtil.isEmpty(items)))
                    .doOnSuccess(items -> {
                        rx.compute(putItemsRx(items)).subscribe();
                        pref.commitCoinListingTime();
                    });
        }
        return Maybe.empty();
    }

    public Maybe<List<Coin>> getRemoteCoinsRx(CoinSource source, List<Long> coinIds) {
        return this.remote.getItemsByCoinIdsRx(coinIds)
                .filter(items -> !(DataUtil.isEmpty(items)))
                .doOnSuccess(items -> {
                    rx.compute(putItemsRx(items)).subscribe();
                });
    }

    //private api
    private Maybe<Coin> getWithSingleSave(Maybe<Coin> source) {
        return source
                .doOnSuccess(item -> {
                    Timber.v("Single update %s", item);
                    if (item != null) {
                        rx.compute(putItemRx(item)).subscribe();
                    }
                });
    }

    private Maybe<List<Coin>> saveRoom(Maybe<List<Coin>> source, Consumer<List<Coin>> onSuccess) {
        Maybe<List<Coin>> maybe = source
                //.onErrorReturnItem(new ArrayList<>())
                .filter(items -> !(DataUtil.isEmpty(items)));
        if (onSuccess != null) {
            maybe = maybe.doOnSuccess(onSuccess);
        }
        return maybe;
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
    }
}
