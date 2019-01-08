package com.dreampany.lca.data.source.remote;

import com.dreampany.lca.api.cmc.model.CmcGraph;

import io.reactivex.Maybe;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public interface CoinMarketCapGraphService {

    @GET("currencies/{slug}/{startTime}/{endTime}/")
    Call<CmcGraph> getGraph(@Path("slug") String slug, @Path("startTime") long startTime, @Path("endTime") long endTime);

    //@Headers("Cache-Control: max-age=640000")
    @GET("currencies/{slug}/{startTime}/{endTime}/")
    Maybe<CmcGraph> getGraphRx(@Path("slug") String slug, @Path("startTime") long startTime, @Path("endTime") long endTime);
}
