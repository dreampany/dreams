package com.dreampany.lca.data.source.room;

import com.dreampany.lca.data.misc.MarketMapper;
import com.dreampany.lca.data.model.Market;
import com.dreampany.lca.data.source.api.MarketDataSource;

import java.util.List;

import javax.inject.Singleton;

import com.dreampany.lca.data.source.dao.MarketDao;
import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 30/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Singleton
public class MarketRoomDataSource implements MarketDataSource {

    private final MarketMapper mapper;
    private final MarketDao dao;

    public MarketRoomDataSource(MarketMapper mapper,
                                MarketDao dao) {
        this.mapper = mapper;
        this.dao = dao;
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
        return dao.getCount();
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
        return dao.insertOrReplace(markets);
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<Market> markets) {
        return Maybe.fromCallable(() -> putItems(markets));
    }

    @Override
    public Market getItem(long id) {
        return null;
    }

    @Override
    public Maybe<Market> getItemRx(long id) {
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
        return null;
    }
}
