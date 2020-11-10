package com.dreampany.tools.data.source.crypto.remote

import android.content.Context
import com.dreampany.framework.misc.func.Keys
import com.dreampany.framework.misc.func.Parser
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.api.crypto.remote.service.CoinMarketCapGraphService
import com.dreampany.tools.data.model.crypto.Graph
import com.dreampany.tools.data.source.crypto.api.GraphDataSource
import com.dreampany.tools.data.source.crypto.mapper.GraphMapper
import java.net.UnknownHostException

/**
 * Created by roman on 10/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class GraphRemoteDataSource
constructor(
private val context: Context,
private val network: NetworkManager,
private val parser: Parser,
private val keys: Keys,
private val mapper : GraphMapper,
private val service: CoinMarketCapGraphService
) : GraphDataSource {

    @Throws
    override suspend fun read(slug: String, startTime: Long, endTime: Long): Graph? {
        try {
             val response= service.read(slug, startTime, endTime).execute()
            if (response.isSuccessful) {
                val data = response.body() ?: return null
                return mapper.read(data)
            } else {
                throw SmartError(

                )
                /*val error = parser.parseError(response, NewsResponse::class)
                throw SmartError(
                    message = error?.status?.errorMessage,
                    code = error?.status?.errorCode
                )*/
            }
        } catch (error: Throwable) {
            if (error is SmartError) throw error
            if (error is UnknownHostException) throw SmartError(
                message = error.message,
                error = error
            )
        }
        throw SmartError()
    }
}