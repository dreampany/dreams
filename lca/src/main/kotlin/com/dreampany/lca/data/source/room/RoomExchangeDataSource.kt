package com.dreampany.lca.data.source.room

import com.dreampany.lca.data.misc.ExchangeMapper
import com.dreampany.lca.data.model.Exchange
import com.dreampany.lca.data.source.api.ExchangeDataSource
import com.dreampany.lca.data.source.dao.ExchangeDao
import io.reactivex.Maybe
import javax.inject.Singleton

/**
 * Created by roman on 2019-07-14
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class RoomExchangeDataSource constructor(
    val mapper: ExchangeMapper,
    val dao: ExchangeDao
) : ExchangeDataSource {

    override fun isEmpty(): Boolean {
        return getCount() == 0
    }

    override fun isEmptyRx(): Maybe<Boolean> {
        return Maybe.fromCallable({ this.isEmpty() })
    }

    override fun getCount(): Int {
        return dao.count
    }

    override fun getCountRx(): Maybe<Int> {
        return Maybe.empty()
    }

    override fun isExists(exchange: Exchange): Boolean {
        return false
    }

    override fun isExistsRx(exchange: Exchange): Maybe<Boolean> {
        return Maybe.empty()
    }

    override fun putItem(exchange: Exchange): Long {
        return dao.insertOrReplace(exchange)
    }

    override fun putItemRx(exchange: Exchange): Maybe<Long> {
        return Maybe.fromCallable { putItem(exchange) }
    }

    override fun putItems(exchanges: List<Exchange>): List<Long> {
        return dao.insertOrReplace(exchanges)
    }

    override fun putItemsRx(exchanges: List<Exchange>): Maybe<List<Long>> {
        return Maybe.fromCallable { putItems(exchanges) }
    }

    override fun delete(exchange: Exchange): Int {
        return 0
    }

    override fun deleteRx(exchange: Exchange): Maybe<Int> {
        return Maybe.empty()
    }

    override fun delete(exchanges: List<Exchange>): List<Long>? {
        return null
    }

    override fun deleteRx(exchanges: List<Exchange>): Maybe<List<Long>> {
        return Maybe.empty()
    }

    override fun getItem(id: String): Exchange? {
        return null
    }

    override fun getItemRx(id: String): Maybe<Exchange> {
        return Maybe.empty()
    }

    override fun getItems(): List<Exchange>? {
        return null
    }

    override fun getItemsRx(): Maybe<List<Exchange>> {
        return Maybe.empty()
    }

    override fun getItems(limit: Int): List<Exchange>? {
        return null
    }

    override fun getItemsRx(limit: Int): Maybe<List<Exchange>> {
        return Maybe.empty()
    }

    override fun getItems(symbol: String, limit: Int): List<Exchange>? {
        return null
    }

    override fun getItemsRx(symbol: String, limit: Int): Maybe<List<Exchange>> {
        return Maybe.empty()
    }
}