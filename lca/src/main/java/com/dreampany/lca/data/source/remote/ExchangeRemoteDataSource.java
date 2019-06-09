package com.dreampany.lca.data.source.remote;

import com.dreampany.lca.api.cc.model.CcExchangeResponse;
import com.dreampany.lca.data.misc.ExchangeMapper;
import com.dreampany.lca.data.model.Exchange;
import com.dreampany.lca.data.source.api.ExchangeDataSource;
import com.dreampany.network.manager.NetworkManager;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.functions.Function;

/**
 * Created by Hawladar Roman on 30/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Singleton
public class ExchangeRemoteDataSource implements ExchangeDataSource {

    private final NetworkManager network;
    private final ExchangeMapper mapper;
    private final CryptoCompareExchangeService service;

    public ExchangeRemoteDataSource(NetworkManager network,
                                    ExchangeMapper mapper,
                                    CryptoCompareExchangeService service) {
        this.network = network;
        this.mapper = mapper;
        this.service = service;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Maybe<Boolean> isEmptyRx() {
        return Maybe.fromCallable(this::isEmpty);
    }

    @Override
    public int getCount() {
        return 0;
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
        return 0;
    }

    @Override
    public Maybe<Long> putItemRx(Exchange exchange) {
        return null;
    }

    @Override
    public List<Long> putItems(List<Exchange> exchanges) {
        return null;
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<Exchange> exchanges) {
        return null;
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
    public Exchange getItem(String id) {
        return null;
    }

    @Override
    public Maybe<Exchange> getItemRx(String id) {
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
    public List<Exchange> getItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<Exchange>> getItemsRx(int limit) {
        return null;
    }

    @Override
    public List<Exchange> getItems(String symbol, int limit) {
        return null;
    }

    @Override
    public Maybe<List<Exchange>> getItemsRx(String symbol, int limit) {
        return service
                .getExchangesRx(symbol, limit)
                .flatMap((Function<CcExchangeResponse, MaybeSource<List<Exchange>>>) this::getItemsRx);
    }

    private Maybe<List<Exchange>> getItemsRx(CcExchangeResponse response) {
        if (response.isSuccess()) {
            return Flowable.fromIterable(response.getData())
                    .map(mapper::toExchange)
                    .toList()
                    .toMaybe();
        }
        return null;
    }
}
