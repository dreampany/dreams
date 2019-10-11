package com.dreampany.tools.data.source.room

import com.dreampany.framework.misc.exception.WriteException
import com.dreampany.tools.data.mapper.StationMapper
import com.dreampany.tools.data.model.Station
import com.dreampany.tools.data.source.api.StationDataSource
import com.dreampany.tools.data.source.room.dao.StationDao
import io.reactivex.Maybe

/**
 * Created by roman on 2019-10-11
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class RoomStationDataSource
constructor(
    private val mapper: StationMapper,
    private val dao: StationDao
) : StationDataSource {
    override fun getItemsByCountryCode(countryCode: String): List<Station>? {
        return dao.getItemsByCountryCode(countryCode)
    }

    override fun getItemsByCountryCodeRx(countryCode: String): Maybe<List<Station>> {
        return dao.getItemsByCountryCodeRx(countryCode)
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

    override fun isExists(t: Station): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExistsRx(t: Station): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItem(t: Station): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItemRx(t: Station): Maybe<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItems(ts: List<Station>): List<Long>? {
        return dao.insertOrReplace(ts)
    }

    override fun putItemsRx(ts: List<Station>): Maybe<List<Long>> {
        return Maybe.create { emitter ->
            val result = putItems(ts)
            if (emitter.isDisposed) return@create
            if (result.isNullOrEmpty()) {
                emitter.onError(WriteException())
            } else {
                emitter.onSuccess(result)
            }
        }
    }

    override fun delete(t: Station): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(t: Station): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(ts: List<Station>): List<Long>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(ts: List<Station>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(id: String): Station? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemRx(id: String): Maybe<Station> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(): List<Station>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(): Maybe<List<Station>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(limit: Long): List<Station>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(limit: Long): Maybe<List<Station>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}