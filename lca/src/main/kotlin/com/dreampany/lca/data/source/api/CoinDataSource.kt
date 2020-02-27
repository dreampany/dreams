package com.dreampany.lca.data.source.api

import com.dreampany.framework.data.source.api.DataSource
import com.dreampany.lca.data.enums.CoinSource
import com.dreampany.lca.data.enums.Currency
import com.dreampany.lca.data.model.Coin
import io.reactivex.Maybe

/**
 * Created by roman on 2019-07-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface CoinDataSource : DataSource<Coin> {

    fun isEmpty(source: CoinSource, currency: Currency, index: Int, limit: Int): Boolean

    fun getRandomItem(source: CoinSource, currency: Currency): Coin?

    fun getItems(
        source: CoinSource,
        currency: Currency,
        index: Int,
        limit: Int
    ): List<Coin>?

    fun getItemsRx(
        source: CoinSource,
        currency: Currency,
        index: Int,
        limit: Int
    ): Maybe<List<Coin>>

    fun getItems(source: CoinSource, currency: Currency): List<Coin>?

    fun getItemsRx(source: CoinSource, currency: Currency): Maybe<List<Coin>>

    fun getItems(source: CoinSource, currency: Currency, limit: Int): List<Coin>?

    fun getItemsRx(source: CoinSource, currency: Currency, limit: Int): Maybe<List<Coin>>

    fun getItem(source: CoinSource, currency: Currency, id: String): Coin?

    fun getItemRx(source: CoinSource, currency: Currency, id: String): Maybe<Coin>

    fun getItems(source: CoinSource, currency: Currency, ids: List<String>): List<Coin>?

    fun getItemsRx(
        source: CoinSource,
        currency: Currency,
        ids: List<String>
    ): Maybe<List<Coin>>
}