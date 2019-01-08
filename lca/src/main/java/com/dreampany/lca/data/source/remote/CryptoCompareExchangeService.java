package com.dreampany.lca.data.source.remote;

import com.dreampany.lca.api.cc.model.CcExchangeResponse;

import io.reactivex.Maybe;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public interface CryptoCompareExchangeService {
    @GET("top/pairs")
    Call<CcExchangeResponse> getExchanges(@Query("fsym") String symbol, @Query("limit") int limit);

    @GET("top/pairs")
    Maybe<CcExchangeResponse> getExchangesRx(@Query("fsym") String symbol, @Query("limit") int limit);
}
