package com.dreampany.lca.data.source.repository;

import com.dreampany.framework.data.source.repository.Repository;
import com.dreampany.framework.misc.ResponseMapper;
import com.dreampany.framework.misc.Room;
import com.dreampany.framework.misc.RxMapper;
import com.dreampany.lca.data.model.Price;
import com.dreampany.lca.data.source.api.PriceDataSource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Singleton
public class PriceRepository extends Repository<Long, Price> implements PriceDataSource {

    private final PriceDataSource local;

    @Inject
    PriceRepository(RxMapper rx,
                    ResponseMapper rm,
                    @Room PriceDataSource local) {
        super(rx, rm);
        this.local = local;
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
        return local.getCountRx();
    }

    @Override
    public Price getItem(String id) {
        return null;
    }

    @Override
    public Maybe<Price> getItemRx(String id) {
        return null;
    }

    @Override
    public boolean isExists(Price price) {
        return false;
    }

    @Override
    public Maybe<Boolean> isExistsRx(Price price) {
        return null;
    }

    @Override
    public long putItem(Price price) {
        return 0;
    }

    @Override
    public Maybe<Long> putItemRx(Price price) {
        return null;
    }

    @Override
    public List<Long> putItems(List<? extends Price> prices) {
        return null;
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<? extends Price> prices) {
        return null;
    }

    @Override
    public int delete(Price price) {
        return 0;
    }

    @Override
    public Maybe<Integer> deleteRx(Price price) {
        return null;
    }

    @Override
    public List<Long> delete(List<? extends Price> prices) {
        return null;
    }

    @Override
    public Maybe<List<Long>> deleteRx(List<? extends Price> prices) {
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
}
