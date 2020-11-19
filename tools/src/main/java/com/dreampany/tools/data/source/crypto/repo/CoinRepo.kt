package com.dreampany.tools.data.source.crypto.repo

import com.dreampany.framework.inject.annote.Remote
import com.dreampany.framework.inject.annote.Room
import com.dreampany.tools.data.model.crypto.Currency
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.source.crypto.api.CoinDataSource
import com.dreampany.tools.data.source.crypto.mapper.CoinMapper
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
    override suspend fun write(input: Coin): Long {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun write(inputs: List<Coin>): List<Long>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun reads(): List<Coin>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun reads(currency: Currency, ids: List<String>): List<Coin>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun reads(
        currency: Currency,
        sort: String,
        order: String,
        offset: Long,
        limit: Long
    ): List<Coin>? = withContext(Dispatchers.IO) {
        if (mapper.isExpired(currency, sort, order, offset)) {
            val result = remote.reads(currency, sort, order, offset, limit)
            if (!result.isNullOrEmpty()) {
                mapper.writeExpire(currency, sort, order, offset)
                room.write(result)
            }
        }
        room.reads(currency, sort, order, offset, limit)
    }

    @Throws
    override suspend fun read(currency: Currency, id: String) = withContext(Dispatchers.IO) {
        if (mapper.isExpired(id, currency)) {
            val result = remote.read(currency, id)
            if (result != null) {
                mapper.writeExpire(id, currency)
                room.write(result)
            }
        }
        room.read(currency, id)
    }

    @Throws
    override suspend fun readsFavorite(
        currency: Currency,
        sort: String,
        order: String
    ) = withContext(Dispatchers.IO) {
        room.readsFavorite(currency, sort, order)
    }

}