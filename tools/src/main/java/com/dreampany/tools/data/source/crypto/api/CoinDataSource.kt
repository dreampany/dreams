package com.dreampany.tools.data.source.crypto.api

import com.dreampany.tools.data.model.crypto.Currency
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.model.crypto.Quote

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
    suspend fun favorites(currency: Currency, sort: String, order: String): List<Pair<Coin, Quote>>?

    @Throws
    suspend fun write(input: Pair<Coin, Quote>): Long

    @Throws
    suspend fun write(inputs: List<Pair<Coin, Quote>>): List<Long>?

    @Throws
    suspend fun read(id: String, currency: Currency): Pair<Coin, Quote>?

    @Throws
    suspend fun reads(): List<Pair<Coin, Quote>>?

    @Throws
    suspend fun reads(ids: List<String>, currency: Currency): List<Pair<Coin, Quote>>?

    @Throws
    suspend fun reads(
        currency: Currency,
        sort: String,
        order: String,
        offset: Long,
        limit: Long
    ): List<Pair<Coin, Quote>>?
}