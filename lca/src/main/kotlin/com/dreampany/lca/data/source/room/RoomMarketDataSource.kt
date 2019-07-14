package com.dreampany.lca.data.source.room

import com.dreampany.lca.data.misc.MarketMapper
import com.dreampany.lca.data.misc.NewsMapper
import com.dreampany.lca.data.model.Market
import com.dreampany.lca.data.source.api.MarketDataSource
import com.dreampany.lca.data.source.dao.MarketDao
import com.dreampany.lca.data.source.dao.NewsDao
import io.reactivex.Maybe
import javax.inject.Singleton

/**
 * Created by roman on 2019-07-14
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class RoomMarketDataSource constructor(
    val mapper: MarketMapper,
    val dao: MarketDao
) : MarketDataSource {

    override fun isEmpty(): Boolean {
        return getCount() == 0
    }

    override fun isEmptyRx(): Maybe<Boolean> {
        return Maybe.fromCallable( { this.isEmpty() })
    }

    override fun getCount(): Int {
        return dao.count
    }

    override fun getCountRx(): Maybe<Int> {
        return Maybe.empty()
    }

    override fun isExists(market: Market): Boolean {
        return false
    }

    override fun isExistsRx(market: Market): Maybe<Boolean> {
        return Maybe.empty()
    }

    override fun putItem(market: Market): Long {
        return 0
    }

    override fun putItemRx(market: Market): Maybe<Long> {
        return Maybe.empty()
    }

    override fun putItems(markets: List<Market>): List<Long> {
        return dao.insertOrReplace(markets)
    }

    override fun putItemsRx(markets: List<Market>): Maybe<List<Long>> {
        return Maybe.fromCallable { putItems(markets) }
    }

    override fun delete(market: Market): Int {
        return 0
    }

    override fun deleteRx(market: Market): Maybe<Int> {
        return Maybe.empty()
    }

    override  fun delete(markets: List<Market>): List<Long>? {
        return null
    }

    override fun deleteRx(markets: List<Market>): Maybe<List<Long>> {
        return Maybe.empty()
    }

    override fun getItem(id: String): Market? {
        return null
    }

    override fun getItemRx(id: String): Maybe<Market> {
        return Maybe.empty()
    }

    override fun getItems(): List<Market>? {
        return null
    }

    override fun getItemsRx(): Maybe<List<Market>> {
        return Maybe.empty()
    }

    override fun getItems(limit: Int): List<Market>? {
        return null
    }

    override fun getItemsRx(limit: Int): Maybe<List<Market>> {
        return Maybe.empty()
    }

    override fun getItems(fromSymbol: String, toSymbol: String, limit: Int): List<Market>? {
        return null
    }

    override fun getItemsRx(fromSymbol: String, toSymbol: String, limit: Int): Maybe<List<Market>> {
        return Maybe.empty()
    }
}