package com.dreampany.crypto.api.remote.service

import com.dreampany.crypto.api.misc.Constants
import com.dreampany.crypto.api.remote.response.gecko.GeckoTickersResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by roman on 11/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface GeckoService {
    @GET(Constants.Apis.Gecko.TICKERS)
    fun tickers(
        @HeaderMap headers: Map<String, String>,
        @Path(Constants.Apis.Gecko.ID) id: String,
        @Query(Constants.Apis.Gecko.INCLUDE_IMAGE) includeImage: Boolean
    ): Call<GeckoTickersResponse>
}