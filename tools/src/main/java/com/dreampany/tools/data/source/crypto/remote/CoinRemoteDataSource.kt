package com.dreampany.tools.data.source.crypto.remote

import android.content.Context
import androidx.annotation.IntRange
import com.dreampany.common.data.enums.Order
import com.dreampany.common.misc.extension.isDebug
import com.dreampany.common.misc.func.Keys
import com.dreampany.common.misc.func.Parser
import com.dreampany.common.misc.func.SmartError
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.api.crypto.misc.CryptoConstants
import com.dreampany.tools.api.crypto.remote.response.CoinsResponse
import com.dreampany.tools.api.crypto.remote.service.CoinMarketCapService
import com.dreampany.tools.data.enums.CoinSort
import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.source.crypto.api.CoinDataSource
import com.dreampany.tools.data.source.crypto.mapper.CoinMapper
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
            keys.setKeys(CryptoConstants.CoinMarketCap.CMC_PRO_ROMAN_BJIT)
        } else {
            keys.setKeys(
                CryptoConstants.CoinMarketCap.CMC_PRO_DREAM_DEBUG_2,
                CryptoConstants.CoinMarketCap.CMC_PRO_DREAM_DEBUG_1,
                CryptoConstants.CoinMarketCap.CMC_PRO_ROMAN_BJIT,
                CryptoConstants.CoinMarketCap.CMC_PRO_IFTE_NET,
                CryptoConstants.CoinMarketCap.CMC_PRO_DREAMPANY
            )
        }
    }

    override suspend fun putItem(item: Coin): Long {
        TODO("Not yet implemented")
    }

    override suspend fun putItems(items: List<Coin>): List<Long>? {
        TODO("Not yet implemented")
    }

    override suspend fun getItems(): List<Coin>? {
        TODO("Not yet implemented")
    }

    override suspend fun getItems(ids: List<String>, currency: Currency): List<Coin>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun getItems(
        currency: Currency,
        sort: CoinSort,
        order: Order,
        @IntRange(from = 0, to = Long.MAX_VALUE)
        offset: Long,
        limit: Long
    ): List<Coin> {
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
                    val data = response.body()?.data ?: throw SmartError(code = response.code())
                    return mapper.getItems(data)
                } else {
                    val error = parser.parseError(response, CoinsResponse::class)
                    throw SmartError(
                        message = error?.status?.errorMessage,
                        code = error?.status?.errorCode
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

    override suspend fun getItem(id: String, currency: Currency): Coin? {
        TODO("Not yet implemented")
    }

    fun getHeader(key: String): Map<String, String> {
        val header = Maps.newHashMap<String, String>()
        header.put(
            CryptoConstants.CoinMarketCap.ACCEPT,
            CryptoConstants.CoinMarketCap.ACCEPT_JSON
        )
        header.put(CryptoConstants.CoinMarketCap.API_KEY, key)
        return header
    }
}