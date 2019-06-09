package com.dreampany.lca.data.source.remote;

import com.dreampany.lca.api.cmc.model.CmcCoinResponse;
import com.dreampany.lca.api.cmc.model.CmcCoinsResponse;

import io.reactivex.Maybe;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public interface CoinMarketCapService {

    @GET("ticker/{id}/")
    Call<CmcCoinResponse> getCoin(@Path("id") String id, @Query("structure") String structure);

    @GET("ticker/{id}/")
    Maybe<CmcCoinResponse> getCoinRx(@Path("id") String id, @Query("structure") String structure);

    @GET("ticker/")
    Call<CmcCoinsResponse> getCoins(@Query("structure") String structure);

    @GET("ticker/")
    Maybe<CmcCoinsResponse> getCoinsRx(@Query("structure") String structure);

    @GET("ticker/")
    Call<CmcCoinsResponse> getCoins(@Query("start") int start, @Query("structure") String structure);

    @GET("ticker/")
    Maybe<CmcCoinsResponse> getCoinsRx(@Query("start") int start, @Query("structure") String structure);

    @GET("ticker/")
    Call<CmcCoinsResponse> getCoins(@Query("start") int start, @Query("limit") int limit, @Query("structure") String structure);

    @GET("ticker/")
    Maybe<CmcCoinsResponse> getCoinsRx(@Query("start") int start, @Query("limit") int limit, @Query("structure") String structure);

    @GET("ticker/")
    Call<CmcCoinsResponse> getCoins(@Query("start") int start, @Query("limit") int limit, @Query("sort") String sort, @Query("structure") String structure);

    @GET("ticker/")
    Maybe<CmcCoinsResponse> getCoinsRx(@Query("start") int start, @Query("limit") int limit, @Query("sort") String sort, @Query("structure") String structure);

    @GET("ticker/")
    Call<CmcCoinsResponse> getCoinsByConverting(@Query("convert") String currency, @Query("limit") int limit);

    @GET("ticker/")
    Maybe<CmcCoinsResponse> getCoinsByConvertingRx(@Query("convert") String currency, @Query("limit") int limit);
}
