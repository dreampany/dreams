package com.dreampany.crypto.api.remote.service

import androidx.annotation.IntRange
import com.dreampany.crypto.api.misc.ApiConstants
import com.dreampany.crypto.api.remote.response.CoinsResponse
import com.dreampany.crypto.api.remote.response.QuotesResponse
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
    @GET(ApiConstants.CoinMarketCap.LISTING)
    fun getListing(
        @HeaderMap headers: Map<String, String>,
        @Query(ApiConstants.CoinMarketCap.CONVERT) currencies: String,
        @Query(ApiConstants.CoinMarketCap.SORT) sort: String,
        @Query(ApiConstants.CoinMarketCap.SORT_DIRECTION) sortDirection: String,
        //@Query(Constants.CoinMarketCap.AUXILIARIES) auxiliaries: String,
        @Query(ApiConstants.Common.START)
        @IntRange(from = 1, to = Long.MAX_VALUE)
        start: Long,
        @Query(ApiConstants.Common.LIMIT) limit: Long
    ): Call<CoinsResponse>

    @GET(ApiConstants.CoinMarketCap.QUOTES)
    fun getQuotes(
        @HeaderMap headers: Map<String, String>,
        @Query(ApiConstants.CoinMarketCap.CONVERT) currencies: String,
        @Query(ApiConstants.CoinMarketCap.ID) ids: String // could be comma separated multiple coin_id
    ): Call<QuotesResponse>
}