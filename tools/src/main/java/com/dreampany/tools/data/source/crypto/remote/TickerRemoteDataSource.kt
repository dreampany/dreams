package com.dreampany.tools.data.source.crypto.remote

import android.content.Context
import com.dreampany.framework.misc.func.Keys
import com.dreampany.framework.misc.func.Parser
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.network.manager.NetworkManager
 import com.dreampany.tools.api.crypto.remote.service.GeckoService
import com.dreampany.tools.data.model.crypto.Ticker
import com.dreampany.tools.data.source.crypto.api.TickerDataSource
import com.dreampany.tools.data.source.crypto.mapper.TickerMapper
import com.dreampany.tools.misc.constants.Constants
import com.google.common.collect.Maps
import java.net.UnknownHostException

/**
 * Created by roman on 11/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class TickerRemoteDataSource(
    private val context: Context,
    private val network: NetworkManager,
    private val parser: Parser,
    private val keys: Keys,
    private val mapper: TickerMapper,
    private val service: GeckoService
) : TickerDataSource {

    @Throws
    override suspend fun getTickers(id: String): List<Ticker>? {
        try {
            val response = service.tickers(header, id, true).execute()
            if (response.isSuccessful) {
                val data = response.body()?.tickers ?: return null
                return mapper.getItems(data)
            } else {
                throw SmartError(message = response.message(), code = response.code())
            }
        } catch (error: Throwable) {
            if (error is SmartError) throw error
            if (error is UnknownHostException) throw SmartError(
                message = error.message,
                error = error
            )
            keys.randomForwardKey()
        }
        throw SmartError()
    }


    private val header: Map<String, String>
        get() {
            val header = Maps.newHashMap<String, String>()
            header.put(Constants.Apis.Gecko.ACCEPT, Constants.Apis.Gecko.ACCEPT_JSON)
            return header
        }
}