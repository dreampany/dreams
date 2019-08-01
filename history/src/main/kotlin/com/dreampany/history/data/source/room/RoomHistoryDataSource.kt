package com.dreampany.history.data.source.room

import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.history.data.enums.HistorySource
import com.dreampany.history.data.enums.HistoryType
import com.dreampany.history.data.misc.HistoryMapper
import com.dreampany.history.data.model.History
import com.dreampany.history.data.source.api.HistoryDataSource
import io.reactivex.Maybe
import javax.inject.Singleton

/**
 * Created by roman on 2019-07-24
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class RoomHistoryDataSource(
    val mapper: HistoryMapper,
    val dao: HistoryDao
) : HistoryDataSource {

    override fun getItems(source: HistorySource, type: HistoryType, day: Int, month: Int): List<History>? {
        return dao.getItems(type, day, month)
    }

    override fun getItemsRx(source: HistorySource, type: HistoryType, day: Int, month: Int): Maybe<List<History>> {
        return dao.getItemsRx(type, day, month)
    }

    override fun isEmpty(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isEmptyRx(): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCountRx(): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExists(t: History): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExistsRx(t: History): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItem(t: History): Long {
        return dao.insertOrReplace(t)
    }

    override fun putItemRx(t: History): Maybe<Long> {
        return dao.insertOrReplaceRx(t)
    }

    override fun putItems(ts: List<History>): List<Long>? {
        val result = mutableListOf<Long>()
        ts.forEach {result.add(putItem(it))}
        return result
    }

    override fun putItemsRx(ts: List<History>): Maybe<List<Long>> {
        return Maybe.create { emitter ->
            val result = putItems(ts)
            if (emitter.isDisposed) {
                return@create
            }
            if (result.isNullOrEmpty()) {
                emitter.onError(EmptyException())
            } else {
                emitter.onSuccess(result)
            }
        }
    }

    override fun delete(t: History): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(t: History): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(ts: List<History>): List<Long>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(ts: List<History>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(id: String): History? {
        return dao.getItem(id)
    }

    override fun getItemRx(id: String): Maybe<History> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(): List<History>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(): Maybe<List<History>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(limit: Int): List<History>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(limit: Int): Maybe<List<History>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}