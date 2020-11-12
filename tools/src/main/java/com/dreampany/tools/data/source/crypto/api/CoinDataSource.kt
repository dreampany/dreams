package com.dreampany.tools.data.source.crypto.api

import com.dreampany.tools.data.enums.crypto.Currency
import com.dreampany.tools.data.model.crypto.Coin

/**
 * Created by roman on 3/21/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface CoinDataSource {

    @Throws
    suspend fun isFavorite(input: Coin): Boolean

    @Throws
    suspend fun toggleFavorite(input: Coin): Boolean

    @Throws
    suspend fun readsFavorite(
        currency: Currency,
        sort: String,
        order: String
    ): List<Coin>?

    @Throws
    suspend fun write(input: Coin): Long

    @Throws
    suspend fun write(inputs: List<Coin>): List<Long>?

    @Throws
    suspend fun read(currency: Currency, id: String): Coin?

    @Throws
    suspend fun reads(): List<Coin>?

    @Throws
    suspend fun reads(currency: Currency, ids: List<String>): List<Coin>?

    @Throws
    suspend fun reads(
        currency: Currency,
        sort: String,
        order: String,
        offset: Long,
        limit: Long
    ): List<Coin>?
}