package com.dreampany.tools.data.source.crypto.remote

import android.content.Context
import androidx.annotation.IntRange
import com.dreampany.framework.misc.exts.value
import com.dreampany.framework.misc.func.Keys
import com.dreampany.framework.misc.func.Parser
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.api.crypto.remote.response.CoinsResponse
import com.dreampany.tools.api.crypto.remote.response.QuotesResponse
import com.dreampany.tools.api.crypto.remote.service.CoinMarketCapService
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.source.crypto.api.CoinDataSource
import com.dreampany.tools.data.source.crypto.mapper.CoinMapper
import com.dreampany.tools.misc.constants.Constants
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
        /*if (context.isDebug) {
            keys.setKeys(CryptoConstants.CoinMarketCap.CMC_PRO_ROMAN_BJIT)
        } else {

        }*/
        keys.setKeys(
            Constants.Apis.CoinMarketCap.CMC_PRO_DREAM_DEBUG_2,
            Constants.Apis.CoinMarketCap.CMC_PRO_DREAM_DEBUG_1,
            Constants.Apis.CoinMarketCap.CMC_PRO_ROMAN_BJIT,
            Constants.Apis.CoinMarketCap.CMC_PRO_IFTE_NET,
            Constants.Apis.CoinMarketCap.CMC_PRO_DREAMPANY
        )
    }

    @Throws
    override suspend fun reads(
        currency: String,
        sort: String,
        order: String,
        @IntRange(from = 0, to = Long.MAX_VALUE)
        offset: Long,
        limit: Long
    ): List<Coin>? {
        for (index in 0..keys.length) {
            try {
                val key = keys.nextKey ?: continue
                val response: Response<CoinsResponse> = service.reads(
                    key.header,
                    currency,
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

    override suspend fun isFavorite(input: Coin): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFavorite(input: Coin): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun readsFavorite(currency: String, sort: String, order: String): List<Coin>? {
        TODO("Not yet implemented")
    }

    override suspend fun write(input: Coin): Long {
        TODO("Not yet implemented")
    }

    override suspend fun write(inputs: List<Coin>): List<Long>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun read(currency: String, id: String): Coin? {
        for (index in 0..keys.length) {
            try {
                val key = keys.nextKey ?: continue
                val response: Response<QuotesResponse> = service.getQuotes(
                    key.header,
                    currency,
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

    override suspend fun reads(): List<Coin>? {
        TODO("Not yet implemented")
    }

    override suspend fun reads(currency: String, ids: List<String>): List<Coin>? {
        TODO("Not yet implemented")
    }

    private val String.header: Map<String, String>
        get() {
            val header = Maps.newHashMap<String, String>()
            header.put(
                Constants.Apis.CoinMarketCap.ACCEPT,
                Constants.Apis.CoinMarketCap.ACCEPT_JSON
            )
            header.put(Constants.Apis.CoinMarketCap.API_KEY, this)
            return header
        }
}