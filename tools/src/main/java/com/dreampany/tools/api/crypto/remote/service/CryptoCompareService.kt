package com.dreampany.tools.api.crypto.remote.service

import com.dreampany.tools.api.crypto.misc.Constants
import com.dreampany.tools.api.crypto.remote.response.ExchangesResponse
import com.dreampany.tools.api.crypto.remote.response.TradesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query

/**
 * Created by roman on 29/2/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface CryptoCompareService {
    @GET(Constants.CryptoCompare.TRADES)
    fun getTrades(
        @HeaderMap headers: Map<String, String>,
        @Query(Constants.CryptoCompare.EXTRA_PARAMS) extraParams: String,
        @Query(Constants.CryptoCompare.FROM_SYMBOL) fromSymbol: String,
        @Query(Constants.Common.LIMIT) limit: Long
    ): Call<TradesResponse>

    @GET(Constants.CryptoCompare.EXCHANGES)
    fun getExchanges(
        @HeaderMap headers: Map<String, String>,
        @Query(Constants.CryptoCompare.EXTRA_PARAMS) extraParams: String,
        @Query(Constants.CryptoCompare.FROM_SYMBOL) fromSymbol: String,
        @Query(Constants.CryptoCompare.TO_SYMBOL) toSymbol: String,
        @Query(Constants.Common.LIMIT) limit: Long
    ): Call<ExchangesResponse>
}