package com.dreampany.crypto.data.source.repo

import com.dreampany.crypto.data.enums.Currency
import com.dreampany.crypto.data.enums.Sort
import com.dreampany.crypto.data.model.Coin
import com.dreampany.crypto.data.source.api.CoinDataSource
import com.dreampany.crypto.data.source.mapper.CoinMapper
import com.dreampany.crypto.data.source.pref.AppPref
import com.dreampany.framework.data.enums.Order
import com.dreampany.framework.inject.annote.Remote
import com.dreampany.framework.inject.annote.Room
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.RxMapper
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
    private val pref: AppPref,
    private val mapper: CoinMapper,
    @Room private val room: CoinDataSource,
    @Remote private val remote: CoinDataSource
) : CoinDataSource {
    @Throws
    override suspend fun isFavorite(input: Coin) = withContext(Dispatchers.IO) {
        room.isFavorite(input)
    }

    override suspend fun toggleFavorite(input: Coin) = withContext(Dispatchers.IO) {
        room.toggleFavorite(input)
    }

    @Throws
    override suspend fun put(input: Coin): Long {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun put(inputs: List<Coin>): List<Long>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun gets(): List<Coin>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun gets(ids: List<String>, currency: Currency): List<Coin>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun gets(
        currency: Currency,
        sort: Sort,
        order: Order,
        offset: Long,
        limit: Long
    ) = withContext(Dispatchers.IO) {
        if (mapper.isExpired(currency, sort, order, offset)) {
            val result = remote.gets(currency, sort, order, offset, limit)
            if (!result.isNullOrEmpty()) {
                mapper.commitExpire(currency, sort, order, offset)
                room.put(result)
            }
        }
        room.gets(currency, sort, order, offset, limit)
    }

    @Throws
    override suspend fun get(id: String, currency: Currency) = withContext(Dispatchers.IO) {
        if (mapper.isExpired(id, currency)) {
            val result = remote.get(id, currency)
            if (result != null) {
                mapper.commitExpire(id, currency)
                room.put(result)
            }
        }
        room.get(id, currency)
    }

    @Throws
    override suspend fun getFavorites(
        currency: Currency,
        sort: Sort,
        order: Order
    ) = withContext(Dispatchers.IO) {
        room.getFavorites(currency, sort, order)
    }

}