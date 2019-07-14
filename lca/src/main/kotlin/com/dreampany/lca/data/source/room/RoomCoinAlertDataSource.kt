package com.dreampany.lca.data.source.room

import com.dreampany.lca.data.misc.CoinAlertMapper
import com.dreampany.lca.data.model.CoinAlert
import com.dreampany.lca.data.source.api.CoinAlertDataSource
import com.dreampany.lca.data.source.dao.CoinAlertDao
import io.reactivex.Maybe
import javax.inject.Singleton

/**
 * Created by roman on 2019-07-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class RoomCoinAlertDataSource constructor(val mapper: CoinAlertMapper, val dao: CoinAlertDao) :
    CoinAlertDataSource {
    override fun isEmpty(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isEmptyRx(): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCountRx(): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItemRx(t: CoinAlert): Maybe<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItems(ts: List<CoinAlert>): List<Long>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItemsRx(ts: List<CoinAlert>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(t: CoinAlert): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(ts: List<CoinAlert>): List<Long>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(ts: List<CoinAlert>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemRx(id: String): Maybe<CoinAlert> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(limit: Int): List<CoinAlert>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(limit: Int): Maybe<List<CoinAlert>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExists(id: String): Boolean {
        return dao.getCount(id) > 0
    }


    override fun getCount(): Int {
        return dao.count
    }

    override fun isExists(coinAlert: CoinAlert): Boolean {
        return dao.getCount(coinAlert.id) > 0
    }

    override fun isExistsRx(coinAlert: CoinAlert): Maybe<Boolean> {
        return Maybe.fromCallable { isExists(coinAlert) }
    }

    override fun putItem(coinAlert: CoinAlert): Long {
        return dao.insertOrReplace(coinAlert)
    }


    override fun delete(coinAlert: CoinAlert): Int {
        return dao.delete(coinAlert)
    }

    override fun getItem(id: String): CoinAlert? {
        return dao.getItem(id)
    }

    override fun getItems(): List<CoinAlert> {
        return dao.items
    }

    override fun getItemsRx(): Maybe<List<CoinAlert>> {
        return Maybe.fromCallable { getItems() }
    }

}