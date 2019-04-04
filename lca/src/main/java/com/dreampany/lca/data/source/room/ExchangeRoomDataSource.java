package com.dreampany.lca.data.source.room;

import com.dreampany.lca.data.misc.ExchangeMapper;
import com.dreampany.lca.data.model.Exchange;
import com.dreampany.lca.data.source.api.ExchangeDataSource;

import java.util.List;

import javax.inject.Singleton;

import com.dreampany.lca.data.source.dao.ExchangeDao;
import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 30/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Singleton
public class ExchangeRoomDataSource implements ExchangeDataSource {

    private final ExchangeMapper mapper;
    private final ExchangeDao dao;

    public ExchangeRoomDataSource(ExchangeMapper mapper,
                                  ExchangeDao dao) {
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
    public boolean isExists(Exchange exchange) {
        return false;
    }

    @Override
    public Maybe<Boolean> isExistsRx(Exchange exchange) {
        return null;
    }

    @Override
    public long putItem(Exchange exchange) {
        return dao.insertOrReplace(exchange);
    }

    @Override
    public Maybe<Long> putItemRx(Exchange exchange) {
        return Maybe.fromCallable(() -> putItem(exchange));
    }

    @Override
    public List<Long> putItems(List<Exchange> exchanges) {
        return dao.insertOrReplace(exchanges);
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<Exchange> exchanges) {
        return Maybe.fromCallable(() -> putItems(exchanges));
    }

    @Override
    public int delete(Exchange exchange) {
        return 0;
    }

    @Override
    public Maybe<Integer> deleteRx(Exchange exchange) {
        return null;
    }

    @Override
    public List<Long> delete(List<Exchange> exchanges) {
        return null;
    }

    @Override
    public Maybe<List<Long>> deleteRx(List<Exchange> exchanges) {
        return null;
    }

    @Override
    public Exchange getItem(long id) {
        return null;
    }

    @Override
    public Maybe<Exchange> getItemRx(long id) {
        return null;
    }

    @Override
    public List<Exchange> getItems() {
        return null;
    }

    @Override
    public Maybe<List<Exchange>> getItemsRx() {
        return null;
    }

    @Override
    public List<Exchange> getItems(long limit) {
        return null;
    }

    @Override
    public Maybe<List<Exchange>> getItemsRx(long limit) {
        return null;
    }

    @Override
    public List<Exchange> getItems(String symbol, int limit) {
        return null;
    }

    @Override
    public Maybe<List<Exchange>> getItemsRx(String symbol, int limit) {
        return null;
    }
}
