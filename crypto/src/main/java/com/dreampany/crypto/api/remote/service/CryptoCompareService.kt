package com.dreampany.crypto.api.remote.service

import com.dreampany.crypto.api.misc.ApiConstants
import com.dreampany.crypto.api.remote.response.ExchangesResponse
import com.dreampany.crypto.api.remote.response.TradesResponse
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
    @GET(ApiConstants.CryptoCompare.TRADES)
    fun getTrades(
        @HeaderMap headers: Map<String, String>,
        @Query(ApiConstants.CryptoCompare.FROM_SYMBOL) fromSymbol: String,
        @Query(ApiConstants.CryptoCompare.EXTRA_PARAMS) extraParams: String,
        @Query(ApiConstants.Common.LIMIT) limit: Long
    ): Call<TradesResponse>

    @GET(ApiConstants.CryptoCompare.EXCHANGES)
    fun getExchanges(
        @HeaderMap headers: Map<String, String>,
        @Query(ApiConstants.CryptoCompare.FROM_SYMBOL) fromSymbol: String,
        @Query(ApiConstants.CryptoCompare.TO_SYMBOL) toSymbol: String,
        @Query(ApiConstants.CryptoCompare.EXTRA_PARAMS) extraParams: String,
        @Query(ApiConstants.Common.LIMIT) limit: Long
    ): Call<ExchangesResponse>
}