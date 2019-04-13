package com.dreampany.lca.data.source.remote;

import com.dreampany.lca.api.iwl.model.IcoResponse;
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
    Call<IcoResponse> getLiveItemsRx();

    @GET("upcoming/")
    Call<IcoResponse> getUpcomingItemsRx();

    @GET("finished/")
    Call<IcoResponse> getFinishedItemsRx();
}
