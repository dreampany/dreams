package com.dreampany.lca.data.source.remote;

import com.dreampany.lca.api.cc.model.CcMarketResponse;

import io.reactivex.Maybe;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public interface CryptoCompareMarketService {
    @GET("top/exchanges/full")
    Call<CcMarketResponse> getMarkets(@Query("fsym") String fromSymbol, @Query("tsym") String toSymbol, @Query("limit") int limit);

    @GET("top/exchanges/full")
    Maybe<CcMarketResponse> getMarketsRx(@Query("fsym") String fromSymbol, @Query("tsym") String toSymbol, @Query("limit") int limit);
}
