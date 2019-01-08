package com.dreampany.lca.data.source.remote;

import com.dreampany.lca.api.iwl.model.FinishedIcoResponse;
import com.dreampany.lca.api.iwl.model.LiveIcoResponse;
import com.dreampany.lca.api.iwl.model.UpcomingIcoResponse;

import io.reactivex.Maybe;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Hawladar Roman on 6/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public interface IcoService {
    @GET("live/")
    Call<LiveIcoResponse> getLiveItems();

    @GET("live/")
    Maybe<LiveIcoResponse> getLiveItemsRx();

    @GET("upcoming/")
    Call<UpcomingIcoResponse> getUpcomingItems();

    @GET("upcoming/")
    Maybe<UpcomingIcoResponse> getUpcomingItemsRx();

    @GET("finished/")
    Call<FinishedIcoResponse> getFinishedItems();

    @GET("finished/")
    Maybe<FinishedIcoResponse> getFinishedItemsRx();
}
