package com.dreampany.tools.data.source.crypto.remote

import android.content.Context
import com.dreampany.common.data.enums.Order
import com.dreampany.common.misc.func.Keys
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.api.crypto.remote.service.CoinMarketCapService
import com.dreampany.tools.data.enums.CoinSort
import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.source.crypto.api.CoinDataSource
import com.dreampany.tools.data.source.crypto.mapper.CoinMapper

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
    private val keys: Keys,
    private val mapper: CoinMapper,
    private val service: CoinMarketCapService
) : CoinDataSource {
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

    override suspend fun getItems(
        currency: Currency,
        sort: CoinSort,
        order: Order,
        offset: Long,
        limit: Long
    ): List<Coin>? {
        TODO("Not yet implemented")
    }

    override suspend fun getItem(id: String, currency: Currency): Coin? {
        TODO("Not yet implemented")
    }
}