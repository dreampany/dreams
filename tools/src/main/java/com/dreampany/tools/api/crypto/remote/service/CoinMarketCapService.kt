package com.dreampany.tools.api.crypto.remote.service

import androidx.annotation.IntRange
import com.dreampany.tools.api.crypto.misc.Constants
import com.dreampany.tools.misc.constants.CryptoConstants
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
    @GET(Constants.Apis.CoinMarketCap.COINS)
    fun reads(
        @HeaderMap headers: Map<String, String>,
        @Query(Constants.Apis.CoinMarketCap.CONVERT) currencies: String,
        @Query(Constants.Apis.CoinMarketCap.SORT) sort: String,
        @Query(Constants.Apis.CoinMarketCap.SORT_DIRECTION) sortDirection: String,
        @Query(CryptoConstants.Common.START)
        @IntRange(from = 1, to = Long.MAX_VALUE)
        start: Long,
        @Query(CryptoConstants.Common.LIMIT) limit: Long
    ): Call<CoinsResponse>

    @GET(CryptoConstants.CoinMarketCap.QUOTES)
    fun getQuotes(
        @HeaderMap headers: Map<String, String>,
        @Query(CryptoConstants.CoinMarketCap.CONVERT) currencies: String,
        @Query(CryptoConstants.CoinMarketCap.ID) ids: String // could be comma separated multiple coin_id
    ): Call<QuotesResponse>
}