package com.dreampany.tools.data.source.crypto.remote

import android.content.Context
import com.dreampany.framework.misc.exts.isDebug
import com.dreampany.framework.misc.exts.value
import com.dreampany.framework.misc.func.Keys
import com.dreampany.framework.misc.func.Parser
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.api.crypto.remote.response.cmc.CurrenciesResponse
import com.dreampany.tools.api.crypto.remote.service.CoinMarketCapService
import com.dreampany.tools.data.model.crypto.Currency
import com.dreampany.tools.data.source.crypto.api.CurrencyDataSource
import com.dreampany.tools.data.source.crypto.mapper.CurrencyMapper
import com.dreampany.tools.misc.constants.Constants
import com.google.common.collect.Maps
import java.net.UnknownHostException

/**
 * Created by roman on 11/18/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CurrencyRemoteDataSource
constructor(
    private val context: Context,
    private val network: NetworkManager,
    private val parser: Parser,
    private val keys: Keys,
    private val mapper: CurrencyMapper,
    private val service: CoinMarketCapService
) : CurrencyDataSource {

    init {
        if (context.isDebug) {
            keys.setKeys(
                Constants.Apis.CoinMarketCap.CMC_PRO_ROMAN_BJIT
            )
        } else {
            keys.setKeys(
                Constants.Apis.CoinMarketCap.CMC_PRO_DREAM_DEBUG_2,
                Constants.Apis.CoinMarketCap.CMC_PRO_DREAM_DEBUG_1,
                Constants.Apis.CoinMarketCap.CMC_PRO_ROMAN_BJIT,
                Constants.Apis.CoinMarketCap.CMC_PRO_IFTE_NET,
                Constants.Apis.CoinMarketCap.CMC_PRO_DREAMPANY
            )
        }
    }

    override suspend fun write(input: Currency): Long {
        TODO("Not yet implemented")
    }

    override suspend fun write(inputs: List<Currency>): List<Long>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun reads(): List<Currency>? {
        for (index in 0..keys.length) {
            try {
                val key = keys.nextKey ?: continue
                val response  = service.currencies(key.header,).execute()
                if (response.isSuccessful) {
                    val data = response.body()?.data ?: return null
                    return mapper.reads(data)
                } else {
                    val error = parser.parseError(response, CurrenciesResponse::class)
                    throw SmartError(
                        message = error?.status?.message,
                        code = error?.status?.code.value
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