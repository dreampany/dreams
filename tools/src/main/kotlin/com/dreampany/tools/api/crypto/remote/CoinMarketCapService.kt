package com.dreampany.tools.api.crypto.remote

import com.dreampany.tools.api.crypto.misc.Constants
import com.dreampany.tools.api.crypto.model.CoinsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by roman on 2019-11-12
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface CoinMarketCapService {
    @GET(Constants.CoinMarketCap.LISTING)
    fun getListing(
        @Query(Constants.CoinMarketCap.API_KEY) key: String,
        @Query(Constants.CoinMarketCap.CONVERT) currencies: String,
        @Query(Constants.Common.START) start: Int,
        @Query(Constants.Common.LIMIT) limit: Int
    ): Call<CoinsResponse>
}