package com.dreampany.history.data.source.remote

import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.history.data.enums.HistoryType
import com.dreampany.history.data.misc.HistoryMapper
import com.dreampany.history.data.model.History
import com.dreampany.history.data.source.api.HistoryDataSource
import com.dreampany.network.manager.NetworkManager
import io.reactivex.Flowable
import io.reactivex.Maybe
import timber.log.Timber
import java.io.IOException
import javax.inject.Singleton

/**
 * Created by roman on 2019-07-24
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class RemoteHistoryDataSource(
    val network: NetworkManager,
    val mapper: HistoryMapper,
    val service: WikiHistoryService
) : HistoryDataSource {

    override fun getItems(type: HistoryType, day: Int, month: Int): List<History>? {
        if (network.isObserving() && !network.hasInternet()) {
            return null
        }
        try {
            val response = service.getWikiHistory(day, month).execute()
            if (response.isSuccessful) {
                val result = response.body()
                return getItems(type, result)
            }
        } catch (error: IOException) {
            Timber.e(error)
        } catch (error: RuntimeException) {
            Timber.e(error)
        }
        return null
    }

    override fun getItemsRx(type: HistoryType, day: Int, month: Int): Maybe<List<History>> {
        return Maybe.create {emitter ->
            val items = getItems(type, day, month)
            if (emitter.isDisposed) {
                return@create
            }
            if (items.isNullOrEmpty()) {
                emitter.onError(EmptyException())
            } else {
                emitter.onSuccess(items)
            }
        }
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    /* private api */
    private fun getItems(type: HistoryType, response: WikiHistoryResponse?): List<History>? {
        if (response == null) {
            return null
        }
        val histories = mutableListOf<History>()
        response.data.run {
            when(type) {
                HistoryType.EVENT -> {
                    events.forEach {
                        histories.add(mapper.toItem(response.date, response.url, it, type))
                    }
                }
                HistoryType.BIRTH -> {
                    births.forEach {
                        histories.add(mapper.toItem(response.date, response.url, it, type))
                    }
                }
                HistoryType.DEATH -> {
                    deaths.forEach {
                        histories.add(mapper.toItem(response.date, response.url, it, type))
                    }
                }
            }
        }
        return histories
    }
}