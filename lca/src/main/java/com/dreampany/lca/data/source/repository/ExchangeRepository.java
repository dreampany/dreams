package com.dreampany.lca.data.source.repository;

import com.dreampany.frame.data.source.repository.Repository;
import com.dreampany.frame.misc.Remote;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.Room;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.lca.data.model.Exchange;
import com.dreampany.lca.data.source.api.ExchangeDataSource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import hugo.weaving.DebugLog;
import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 6/26/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class ExchangeRepository extends Repository<Long, Exchange> implements ExchangeDataSource {

    private final ExchangeDataSource local;
    private final ExchangeDataSource remote;

    @DebugLog
    @Inject
    ExchangeRepository(RxMapper rx,
                       ResponseMapper rm,
                       @Room ExchangeDataSource local,
                       @Remote ExchangeDataSource remote) {
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
    public List<Exchange> getItems(String symbol, long limit) {
        return null;
    }

    @Override
    public Maybe<List<Exchange>> getItemsRx(String symbol, long limit) {
        return getWithSave(remote.getItemsRx(symbol, limit));
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
        return local.putItem(exchange);
    }

    @Override
    public Maybe<Long> putItemRx(Exchange exchange) {
        return Maybe.fromCallable(() -> putItem(exchange));
    }

    @Override
    public List<Long> putItems(List<Exchange> exchanges) {
        return local.putItems(exchanges);
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

    private Maybe<List<Exchange>> getWithSave(Maybe<List<Exchange>> source) {
        return source
                .onErrorReturnItem(new ArrayList<>())
                .filter(items -> !(items == null || items.isEmpty()))
                .doOnSuccess(items -> {
                    rx.compute(putItemsRx(items)).subscribe();
                });
    }
}
