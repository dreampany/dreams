package com.dreampany.tools.api.crypto.remote.service

import com.dreampany.tools.api.crypto.misc.Constants
import com.dreampany.tools.api.crypto.remote.response.CoinsResponse
import com.dreampany.tools.api.crypto.remote.response.QuotesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.HeaderMap
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
        @HeaderMap headers: Map<String, String>,
        @Query(Constants.CoinMarketCap.CONVERT) currencies: String,
        @Query(Constants.CoinMarketCap.SORT) sort: String,
        @Query(Constants.CoinMarketCap.SORT_DIRECTION) sortDirection: String,
        //@Query(Constants.CoinMarketCap.AUXILIARIES) auxiliaries: String,
        @Query(Constants.Common.START) start: Long,
        @Query(Constants.Common.LIMIT) limit: Long
    ): Call<CoinsResponse>

    @GET(Constants.CoinMarketCap.QUOTES)
    fun getQuotes(
        @HeaderMap headers: Map<String, String>,
        @Query(Constants.CoinMarketCap.CONVERT) currencies: String,
        @Query(Constants.CoinMarketCap.ID) id: String // could be comma separated multiple coin_id
    ): Call<QuotesResponse>
}