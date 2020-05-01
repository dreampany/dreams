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
    @Throws
    override suspend fun isFavorite(input: Coin) = withContext(Dispatchers.IO) {
        room.isFavorite(input)
    }

    override suspend fun toggleFavorite(input: Coin)= withContext(Dispatchers.IO) {
        room.toggleFavorite(input)
    }

    @Throws
    override suspend fun putItem(input: Coin): Long {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun insert(inputs: List<Coin>): List<Long>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun getCoins(): List<Coin>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun getCoins(ids: List<String>, currency: Currency): List<Coin>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun getCoins(
        currency: Currency,
        sort: CoinSort,
        order: Order,
        offset: Long,
        limit: Long
    ) = withContext(Dispatchers.IO) {
        if (mapper.isExpired(currency, sort, order, offset)) {
            val result = remote.getCoins(currency, sort, order, offset, limit)
            if (!result.isNullOrEmpty()) {
                mapper.commitExpire(currency, sort, order, offset)
                room.insert(result)
            }
        }
        room.getCoins(currency, sort, order, offset, limit)
    }

    @Throws
    override suspend fun getCoin(id: String, currency: Currency) = withContext(Dispatchers.IO) {
        if (mapper.isExpired(id, currency)) {
            val result = remote.getCoin(id, currency)
            if (result != null) {
                mapper.commitExpire(id, currency)
                room.putItem(result)
            }
        }
        room.getCoin(id, currency)
    }

    @Throws
    override suspend fun getFavoriteCoins(
        currency: Currency,
        sort: CoinSort,
        order: Order
    ) = withContext(Dispatchers.IO) {
        room.getFavoriteCoins(currency, sort, order)
    }

}