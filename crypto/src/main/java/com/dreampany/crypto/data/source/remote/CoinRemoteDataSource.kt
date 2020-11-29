package com.dreampany.crypto.data.source.remote

import android.content.Context
import androidx.annotation.IntRange
import com.dreampany.crypto.api.misc.ApiConstants
import com.dreampany.crypto.api.remote.response.CoinsResponse
import com.dreampany.crypto.api.remote.response.QuotesResponse
import com.dreampany.crypto.api.remote.service.CoinMarketCapService
import com.dreampany.crypto.data.enums.Currency
import com.dreampany.crypto.data.enums.Sort
import com.dreampany.crypto.data.model.Coin
import com.dreampany.crypto.data.source.api.CoinDataSource
import com.dreampany.crypto.data.source.mapper.CoinMapper
import com.dreampany.framework.data.enums.Order
import com.dreampany.framework.misc.exts.isDebug
import com.dreampany.framework.misc.exts.value
import com.dreampany.framework.misc.func.Keys
import com.dreampany.framework.misc.func.Parser
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.network.manager.NetworkManager
import com.google.common.collect.Maps
import retrofit2.Response
import java.net.UnknownHostException

/**
 * Created by roman on 3/21/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CoinRemoteDataSource
constructor(
    private val context: Context,
    private val network: NetworkManager,
    private val parser: Parser,
    private val keys: Keys,
    private val mapper: CoinMapper,
    private val service: CoinMarketCapService
) : CoinDataSource {

    init {
        if (context.isDebug) {
            keys.setKeys(
                ApiConstants.CoinMarketCap.CMC_PRO_ROMAN_BJIT
            )
        } else {
            keys.setKeys(
                ApiConstants.CoinMarketCap.CMC_PRO_DREAM_DEBUG_2,
                ApiConstants.CoinMarketCap.CMC_PRO_DREAM_DEBUG_1,
                ApiConstants.CoinMarketCap.CMC_PRO_ROMAN_BJIT,
                ApiConstants.CoinMarketCap.CMC_PRO_IFTE_NET,
                ApiConstants.CoinMarketCap.CMC_PRO_DREAMPANY
            )
        }

    }

    private fun getHeader(key: String): Map<String, String> {
        val header = Maps.newHashMap<String, String>()
        header.put(
            ApiConstants.CoinMarketCap.ACCEPT,
            ApiConstants.CoinMarketCap.ACCEPT_JSON
        )
        header.put(ApiConstants.CoinMarketCap.API_KEY, key)
        return header
    }

    override suspend fun isFavorite(input: Coin): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFavorite(input: Coin): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun put(input: Coin): Long {
        TODO("Not yet implemented")
    }

    override suspend fun put(inputs: List<Coin>): List<Long>? {
        TODO("Not yet implemented")
    }

    override suspend fun gets(): List<Coin>? {
        TODO("Not yet implemented")
    }

    override suspend fun gets(ids: List<String>, currency: Currency): List<Coin>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun gets(
        currency: Currency,
        sort: Sort,
        order: Order,
        @IntRange(from = 0, to = Long.MAX_VALUE)
        offset: Long,
        limit: Long
    ): List<Coin>? {
        for (index in 0..keys.length) {
            try {
                val key = keys.nextKey ?: continue
                val response: Response<CoinsResponse> = service.getListing(
                    getHeader(key),
                    currency.name,
                    sort.value,
                    order.value,
                    offset + 1, //Coin Market Cap start from 1 - IntRange
                    limit
                ).execute()
                if (response.isSuccessful) {
                    val data = response.body()?.data ?: return null
                    return mapper.getItems(data)
                } else {
                    val error = parser.parseError(response, CoinsResponse::class)
                    throw SmartError(
                        message = error?.status?.errorMessage,
                        code = error?.status?.errorCode.value
                    )
                }
            } catch (error: Throwable) {
                if (error is SmartError) throw error
                if (error is UnknownHostException) throw SmartError(
                    message = error.message,
                    error = error
                )
                keys.randomForwardKey()
            }
        }
        throw SmartError()
    }

    @Throws
    override suspend fun get(id: String, currency: Currency): Coin? {
        for (index in 0..keys.length) {
            try {
                val key = keys.nextKey ?: continue
                val response: Response<QuotesResponse> = service.getQuotes(
                    getHeader(key),
                    currency.name,
                    id
                ).execute()
                if (response.isSuccessful) {
                    val data = response.body()?.data ?: return null
                    val inputData = data.get(id) ?: return null
                    return mapper.getItem(inputData)
                } else {
                    val error = parser.parseError(response, QuotesResponse::class)
                    throw SmartError(
                        message = error?.status?.errorMessage,
                        code = error?.status?.errorCode.value
                    )
                }
            } catch (error: Throwable) {
                if (error is SmartError) throw error
                if (error is UnknownHostException) throw SmartError(
                    message = error.message,
                    error = error
                )
                keys.randomForwardKey()
            }
        }
        throw SmartError()
    }

    @Throws
    override suspend fun getFavorites(
        currency: Currency,
        sort: Sort,
        order: Order
    ): List<Coin>? {
        TODO("Not yet implemented")
    }
}