package com.dreampany.tools.data.source.crypto.repo

import com.dreampany.common.data.enums.Order
import com.dreampany.common.inject.annote.*
import com.dreampany.common.misc.func.ResponseMapper
import com.dreampany.common.misc.func.RxMapper
import com.dreampany.tools.data.enums.CoinSort
import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.source.crypto.api.CoinDataSource
import com.dreampany.tools.data.source.crypto.mapper.CoinMapper
import com.dreampany.tools.data.source.crypto.pref.CryptoPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 3/21/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class CoinRepo
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    private val pref: CryptoPref,
    private val mapper: CoinMapper,
    @Room private val room: CoinDataSource,
    @Remote private val remote: CoinDataSource
) : CoinDataSource {
    override suspend fun getCoins(
        currency: Currency,
        sort: CoinSort,
        order: Order,
        start: Long,
        limit: Long
    ) = withContext(Dispatchers.IO) {
        if (mapper.isExpired(currency, sort, order, start)) {
            val result = remote.getCoins(currency, sort, order, start, limit)
            if (result != null) {

            }
        }
        room.getCoins(currency, sort, order, start, limit)
    }

    override suspend fun getCoins(currency: Currency, ids: List<String>): List<Coin>? {
        TODO("Not yet implemented")
    }

    override suspend fun getCoin(currency: Currency, id: String): Coin? {
        TODO("Not yet implemented")
    }
}