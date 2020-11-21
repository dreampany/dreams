package com.dreampany.tools.api.crypto.remote.service

import androidx.annotation.IntRange
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.tools.api.crypto.misc.Constants
import com.dreampany.tools.api.crypto.remote.response.cmc.CoinsResponse
import com.dreampany.tools.api.crypto.remote.response.cmc.CurrenciesResponse
import com.dreampany.tools.api.crypto.remote.response.cmc.QuotesResponse
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

    @GET(Constants.Apis.CoinMarketCap.CURRENCIES)
    fun currencies(
        @HeaderMap headers: Map<String, String>,
        @Query(Constants.Keys.CMC.METALS) metals: Boolean = true,
        @IntRange(from = 1, to = Long.MAX_VALUE)
        @Query(Constants.Keys.Common.LIMIT) limit: Long = Long.MAX_VALUE
    ): Call<CurrenciesResponse>

    @GET(Constants.Apis.CoinMarketCap.COINS)
    fun coins(
        @HeaderMap headers: Map<String, String>,
        @Query(Constants.Keys.CMC.CONVERT_ID) convertIds: String,
        @Query(Constants.Keys.CMC.SORT) sort: String,
        @Query(Constants.Keys.CMC.SORT_DIRECTION) order: String,
        @IntRange(from = 1, to = Long.MAX_VALUE)
        @Query(Constants.Keys.Common.START) start: Long,
        @IntRange(from = 1, to = Long.MAX_VALUE)
        @Query(Constants.Keys.Common.LIMIT) limit: Long
    ): Call<CoinsResponse>

    @GET(Constants.Apis.CoinMarketCap.QUOTES)
    fun quotes(
        @HeaderMap headers: Map<String, String>,
        @Query(Constant.Keys.ID) ids: String,
        @Query(Constants.Keys.CMC.CONVERT_ID) convertIds: String
    ): Call<QuotesResponse>

    /*@GET(Constants.Apis.CoinMarketCap.META)
    fun metas(
        @HeaderMap headers: Map<String, String>,
        @Query(Constant.Keys.ID) ids: String // could be comma separated multiple coin_id
    ): Call<MetasResponse>*/
}