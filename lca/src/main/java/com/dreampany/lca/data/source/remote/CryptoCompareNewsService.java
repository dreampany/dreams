package com.dreampany.lca.data.source.remote;

import com.dreampany.lca.api.cc.model.CcNews;

import java.util.List;

import io.reactivex.Maybe;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Hawladar Roman on 6/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public interface CryptoCompareNewsService {
    @GET("news/")
    Call<List<CcNews>> getNews();

    @GET("news/")
    Maybe<List<CcNews>> getNewsRx();
}
