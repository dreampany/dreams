package com.dreampany.lca.data.source.remote;

import com.dreampany.lca.data.model.Coin;

import java.util.List;

import io.reactivex.Maybe;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public interface CryptoCompareService {
    @GET("listings/")
    Call<List<Coin>> getCoins();

    @GET("listings/")
    Maybe<List<Coin>> getCoinsRx();
}
