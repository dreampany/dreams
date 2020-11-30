package com.dreampany.crypto.api.remote.service

import com.dreampany.crypto.api.misc.Constants
import com.dreampany.crypto.api.model.cmc.CryptoGraph
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
        @Path(Constants.Keys.CMC.SLUG) slug: String,
        @Path(Constants.Keys.CMC.START_TIME) startTime: Long,
        @Path(Constants.Keys.CMC.END_TIME) endTime: Long
     ): Call<CryptoGraph>
}