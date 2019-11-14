package com.dreampany.tools.data.source.room

import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.data.mapper.CoinMapper
import com.dreampany.tools.data.model.Coin
import com.dreampany.tools.data.source.api.CoinDataSource
import com.dreampany.tools.data.source.room.dao.CoinDao
import io.reactivex.Maybe

/**
 * Created by roman on 2019-11-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class RoomCoinDataSource(
    private val mapper: CoinMapper,
    private val dao: CoinDao,
    private val quoteDao: CoinDao
) : CoinDataSource {


    override fun getItems(
        currency: Currency,
        sort: String,
        sortDirection: String,
        auxiliaries: String,
        start: Long,
        limit: Long
    ): List<Coin>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(): List<Coin>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(limit: Long): List<Coin>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(
        currency: Currency,
        sort: String,
        sortDirection: String,
        auxiliaries: String,
        start: Long,
        limit: Long
    ): Maybe<List<Coin>> {

    }

    override fun isEmpty(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isEmptyRx(): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount(): Int {
        return dao.count
    }

    override fun getCountRx(): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExists(t: Coin): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExistsRx(t: Coin): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItem(t: Coin): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItemRx(t: Coin): Maybe<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItems(ts: List<Coin>): List<Long>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItemsRx(ts: List<Coin>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(t: Coin): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(t: Coin): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(ts: List<Coin>): List<Long>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(ts: List<Coin>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(id: String): Coin? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemRx(id: String): Maybe<Coin> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(): Maybe<List<Coin>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(limit: Long): Maybe<List<Coin>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /* private */
    private fun updateCache() {
        if (!cacheLoaded || !mapper.hasCoins()) {
            val room = dao.items
            mapper.add(room)
            cacheLoaded = true
        }
    }
}