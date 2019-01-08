package com.dreampany.lca.data.source.remote;

import com.dreampany.lca.api.cc.model.CcMarketResponse;
import com.dreampany.lca.data.misc.MarketMapper;
import com.dreampany.lca.data.model.Market;
import com.dreampany.lca.data.source.api.MarketDataSource;
import com.dreampany.network.NetworkManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.functions.Function;
import retrofit2.Response;

/**
 * Created by Hawladar Roman on 30/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Singleton
public class MarketRemoteDataSource implements MarketDataSource {

    private final NetworkManager network;
    private final MarketMapper mapper;
    private final CryptoCompareMarketService service;

    public MarketRemoteDataSource(NetworkManager network,
                                  MarketMapper mapper,
                                  CryptoCompareMarketService service) {
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
        return null;
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
/*        return Maybe.fromCallable(new Callable<List<Market>>() {
            @Override
            public List<Market> call() throws Exception {

             Response<CcMarketResponse> response = service.getMarkets(fromSymbol, toSymbol, limit).execute();

             CcMarketResponse data = response.body();
                return new ArrayList<>();
            }
        });*/
        return service.getMarketsRx(fromSymbol, toSymbol, limit)
                .flatMap((Function<CcMarketResponse, MaybeSource<List<Market>>>) this::getItemsRx);
    }

    private Maybe<List<Market>> getItemsRx(CcMarketResponse response) {
        return Flowable.fromIterable(response.getMarkets())
                .map(mapper::toMarket)
                .toList()
                .toMaybe();
    }
}
