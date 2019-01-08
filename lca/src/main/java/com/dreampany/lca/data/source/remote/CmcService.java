package com.dreampany.lca.data.source.remote;

import com.dreampany.lca.api.cmc.model.CmcListingResponse;
import com.dreampany.lca.api.cmc.model.CmcQuotesResponse;

import io.reactivex.Maybe;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public interface CmcService {

    /*    @Headers({
                "Accept: application/json",
                "Accept-Encoding: deflate, gzip"
        })*/
    @GET("cryptocurrency/listings/latest")
    Call<CmcListingResponse> getListing(@Query("CMC_PRO_API_KEY") String apiKey,
                                        @Query("start") int start,
                                        @Query("limit") int limit);

    @GET("cryptocurrency/listings/latest")
    Maybe<CmcListingResponse> getListingRx(@Query("CMC_PRO_API_KEY") String apiKey,
                                           @Query("start") int start,
                                           @Query("limit") int limit);

    @GET("cryptocurrency/quotes/latest")
    Maybe<CmcQuotesResponse> getQuotesRx(@Query("CMC_PRO_API_KEY") String apiKey,
                                         @Query("id") String ids);
}
