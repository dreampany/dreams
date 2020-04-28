package com.dreampany.tools.data.source.crypto.repo

import com.dreampany.common.data.enums.Order
import com.dreampany.common.inject.annote.Remote
import com.dreampany.common.inject.annote.Room
import com.dreampany.common.misc.func.ResponseMapper
import com.dreampany.common.misc.func.RxMapper
import com.dreampany.tools.data.enums.crypto.CoinSort
import com.dreampany.tools.data.enums.crypto.Currency
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
        offset: Long,
        limit: Long
    ) = withContext(Dispatchers.IO) {
        if (mapper.isExpired(currency, sort, order, offset)) {
            val result = remote.getItems(currency, sort, order, offset, limit)
            if (!result.isNullOrEmpty()) {
                mapper.commitExpire(currency, sort, order, offset)
                room.putItems(result)
            }
        }
        room.getItems(currency, sort, order, offset, limit)
    }

    @Throws
    override suspend fun getItem(id: String, currency: Currency) = withContext(Dispatchers.IO) {
        if (mapper.isExpired(id, currency)) {
            val result = remote.getItem(id, currency)
            if (result != null) {
                mapper.commitExpire(id, currency)
                room.putItem(result)
            }
        }
        room.getItem(id, currency)
    }

}