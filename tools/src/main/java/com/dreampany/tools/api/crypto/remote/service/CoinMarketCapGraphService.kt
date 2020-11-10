package com.dreampany.tools.api.crypto.remote.service

import com.dreampany.tools.api.crypto.misc.Constants
import com.dreampany.tools.api.crypto.model.CryptoGraph
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by roman on 10/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface CoinMarketCapGraphService {
    @GET(Constants.Apis.CoinMarketCap.GRAPH)
    fun read(
        @Path(Constants.Keys.CoinMarketCap.SLUG) slug: String,
        @Path(Constants.Keys.CoinMarketCap.START_TIME) startTime: Long,
        @Path(Constants.Keys.CoinMarketCap.END_TIME) endTime: Long
     ): Call<CryptoGraph>
}