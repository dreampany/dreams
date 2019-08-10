package com.dreampany.history.data.source.repository

import com.dreampany.frame.data.source.repository.Repository
import com.dreampany.frame.misc.Remote
import com.dreampany.frame.misc.ResponseMapper
import com.dreampany.frame.misc.Room
import com.dreampany.frame.misc.RxMapper
import com.dreampany.history.data.enums.HistorySource
import com.dreampany.history.data.enums.HistoryType
import com.dreampany.history.data.model.History
import com.dreampany.history.data.source.api.HistoryDataSource
import io.reactivex.Maybe
import io.reactivex.internal.functions.Functions
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Roman-372 on 7/25/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class HistoryRepository
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    @Room private val room: HistoryDataSource,
    @Remote private val remote: HistoryDataSource
) : Repository<String, History>(rx, rm), HistoryDataSource {

    override fun getItems(source: HistorySource, type: HistoryType, day: Int, month: Int): List<History>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(): List<History>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(limit: Int): List<History>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(source: HistorySource, type: HistoryType, day: Int, month: Int): Maybe<List<History>> {
        val room = this.room.getItemsRx(source, type, day, month)
        val remote = this.remote.getItemsRx(source, type, day, month)
            .filter{ !it.isNullOrEmpty() }
            .doOnSuccess {
                rx.compute(this.room.putItemsRx(it)).subscribe(
                    Functions.emptyConsumer<List<Long>>(),
                    Functions.emptyConsumer<Throwable>()
                )
            }
       return concatFirstRx(true, room, remote)
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItemRx(t: History): Maybe<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItems(ts: List<History>): List<Long>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItemsRx(ts: List<History>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        return room.getItem(id)
    }

    override fun getItemRx(id: String): Maybe<History> {
        val room = this.room.getItemRx(id)
        val remote = this.remote.getItemRx(id)
            .filter{ it != null }
            .doOnSuccess {
                rx.compute(this.room.putItemRx(it)).subscribe(
                    Functions.emptyConsumer<Long>(),
                    Functions.emptyConsumer<Throwable>()
                )
            }
        return concatSingleFirstRx(room, remote)
    }

    override fun getItemsRx(): Maybe<List<History>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(limit: Int): Maybe<List<History>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}