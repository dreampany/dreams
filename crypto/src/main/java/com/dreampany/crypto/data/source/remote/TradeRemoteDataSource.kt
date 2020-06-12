package com.dreampany.crypto.data.source.remote

import android.content.Context
import com.dreampany.crypto.api.misc.ApiConstants
import com.dreampany.crypto.api.remote.service.CryptoCompareService
import com.dreampany.crypto.data.model.Trade
import com.dreampany.crypto.data.source.api.TradeDataSource
import com.dreampany.crypto.data.source.mapper.TradeMapper
import com.dreampany.framework.misc.exts.isDebug
import com.dreampany.framework.misc.func.Keys
import com.dreampany.framework.misc.func.Parser
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.network.manager.NetworkManager
import com.google.common.collect.Maps
import java.net.UnknownHostException

/**
 * Created by roman on 3/21/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class TradeRemoteDataSource
constructor(
    private val context: Context,
    private val network: NetworkManager,
    private val parser: Parser,
    private val keys: Keys,
    private val mapper: TradeMapper,
    private val service: CryptoCompareService
) : TradeDataSource {

    init {
        if (context.isDebug) {
            keys.setKeys(ApiConstants.CryptoCompare.API_KEY_ROMAN_BJIT)
        } else {
            keys.setKeys(ApiConstants.CryptoCompare.API_KEY_ROMAN_BJIT)
        }
    }

    private fun getHeader(key: String): Map<String, String> {
        val header = Maps.newHashMap<String, String>()
        header.put(
            ApiConstants.CoinMarketCap.ACCEPT,
            ApiConstants.CoinMarketCap.ACCEPT_JSON
        )
        header.put(ApiConstants.CryptoCompare.AUTHORIZATION, key)
        return header
    }

    @Throws
    override suspend fun getTrades(fromSymbol: String, extraParams: String, limit: Long): List<Trade>? {
        for (index in 0..keys.length) {
            try {
                val key = keys.nextKey ?: continue
                val response = service.getTrades(
                    getHeader(key),
                    fromSymbol,
                    extraParams,
                    limit
                ).execute()
                if (response.isSuccessful) {
                    val data = response.body()?.data ?: return null
                    return mapper.getItems(data)
                } else {
                    //val error = parser.parseError(response, TradesResponse::class)
                    throw SmartError()
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
}