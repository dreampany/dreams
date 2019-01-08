package com.dreampany.lca.data.source.room;

import com.dreampany.lca.data.misc.PriceMapper;
import com.dreampany.lca.data.model.Price;
import com.dreampany.lca.data.source.api.PriceDataSource;
import com.dreampany.lca.data.source.local.PriceDao;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 7/23/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class PriceRoomDataSource implements PriceDataSource {

    private final PriceMapper mapper;
    private final PriceDao dao;

    public PriceRoomDataSource(PriceMapper mapper,
                               PriceDao dao) {
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
    public boolean isExists(Price price) {
        return dao.getCount(price.getId()) > 0;
    }

    @Override
    public Maybe<Boolean> isExistsRx(Price price) {
        return Maybe.fromCallable(() -> isExists(price));
    }

    @Override
    public long putItem(Price price) {
        return dao.insertOrReplace(price);
    }

    @Override
    public Maybe<Long> putItemRx(Price price) {
        return Maybe.fromCallable(() -> dao.insertOrReplace(price));
    }

    @Override
    public List<Long> putItems(List<Price> prices) {
        return null;
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<Price> prices) {
        return null;
    }

    @Override
    public Price getItem(long id) {
        return null;
    }

    @Override
    public Maybe<Price> getItemRx(long id) {
        return null;
    }

    @Override
    public List<Price> getItems() {
        return null;
    }

    @Override
    public Maybe<List<Price>> getItemsRx() {
        return null;
    }

    @Override
    public List<Price> getItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<Price>> getItemsRx(int limit) {
        return null;
    }

    @Override
    public Price getItem(String id) {
        return null;
    }

    @Override
    public Maybe<Price> getItemRx(String id) {
        return null;
    }
}
