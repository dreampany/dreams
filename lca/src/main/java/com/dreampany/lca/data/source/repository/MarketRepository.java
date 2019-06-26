package com.dreampany.lca.data.source.repository;

import com.dreampany.frame.data.source.repository.Repository;
import com.dreampany.frame.misc.Room;
import com.dreampany.frame.misc.Remote;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.lca.data.model.Market;
import com.dreampany.lca.data.source.api.MarketDataSource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;


import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 6/26/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class MarketRepository extends Repository<Long, Market> implements MarketDataSource {

    private final MarketDataSource local;
    private final MarketDataSource remote;


    @Inject
    MarketRepository(RxMapper rx,
                     ResponseMapper rm,
                     @Room MarketDataSource local,
                     @Remote MarketDataSource remote) {
        super(rx, rm);
        this.local = local;
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
        return local.getCount();
    }

    @Override
    public Maybe<Integer> getCountRx() {
        return null;
    }

    @Override
    public boolean isExists(Market market) {
        return false;
    }

    @Override
    public Maybe<Boolean> isExistsRx(Market market) {
        return null;
    }

    @Override
    public long putItem(Market market) {
        return 0;
    }

    @Override
    public Maybe<Long> putItemRx(Market market) {
        return null;
    }

    @Override
    public List<Long> putItems(List<Market> markets) {
        return null;
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<Market> markets) {
        return local.putItemsRx(markets);
    }

    @Override
    public int delete(Market market) {
        return 0;
    }

    @Override
    public Maybe<Integer> deleteRx(Market market) {
        return null;
    }

    @Override
    public List<Long> delete(List<Market> markets) {
        return null;
    }

    @Override
    public Maybe<List<Long>> deleteRx(List<Market> markets) {
        return null;
    }

    @Override
    public Market getItem(String id) {
        return null;
    }

    @Override
    public Maybe<Market> getItemRx(String id) {
        return null;
    }

    @Override
    public List<Market> getItems() {
        return null;
    }

    @Override
    public Maybe<List<Market>> getItemsRx() {
        return null;
    }

    @Override
    public List<Market> getItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<Market>> getItemsRx(int limit) {
        return null;
    }

    @Override
    public List<Market> getItems(String fromSymbol, String toSymbol, int limit) {
        return null;
    }

    @Override
    public Maybe<List<Market>> getItemsRx(String fromSymbol, String toSymbol, int limit) {
        return getWithSave(remote.getItemsRx(fromSymbol, toSymbol, limit));
    }

    private Maybe<List<Market>> getWithSave(Maybe<List<Market>> Maybe) {
        return Maybe
                .filter(items -> !(DataUtil.isEmpty(items)))
                .doOnSuccess(items -> rx.compute(putItemsRx(items)).subscribe());
    }
}
