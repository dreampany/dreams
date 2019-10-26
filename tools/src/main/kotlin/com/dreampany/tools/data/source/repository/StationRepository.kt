package com.dreampany.tools.data.source.repository

import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.misc.StoreMapper
import com.dreampany.framework.data.source.repository.Repository
import com.dreampany.framework.data.source.repository.StoreRepository
import com.dreampany.framework.misc.Remote
import com.dreampany.framework.misc.ResponseMapper
import com.dreampany.framework.misc.Room
import com.dreampany.framework.misc.RxMapper
import com.dreampany.framework.misc.exception.EmptyException
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.mapper.StationMapper
import com.dreampany.tools.data.model.Station
import com.dreampany.tools.data.source.api.StationDataSource
import io.reactivex.Maybe
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-10-11
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class StationRepository
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    private val network: NetworkManager,
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepository,
    private val mapper: StationMapper,
    @Room private val room: StationDataSource,
    @Remote private val remote: StationDataSource
) : Repository<String, Station>(rx, rm), StationDataSource {
    override fun getItemsOfTrends(limit: Long): List<Station>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsOfTrendsRx(limit: Long): Maybe<List<Station>> {
        val remoteIf = getRemoteItemsOfTrendsIfRx(limit)
        val roomAny = room.getItemsOfTrendsRx(limit)
        return concatFirstRx(true, remoteIf, roomAny)
    }

    override fun getItemsOfPopular(limit: Long): List<Station>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsOfPopularRx(limit: Long): Maybe<List<Station>> {
        val remoteIf = getRemoteItemsOfPopularIfRx(limit)
        val roomAny = room.getItemsOfPopularRx(limit)
        return concatFirstRx(true, remoteIf, roomAny)
    }

    override fun getItemsOfCountry(countryCode: String, limit: Long): List<Station>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsOfCountryRx(countryCode: String, limit: Long): Maybe<List<Station>> {
        val remoteIf = getRemoteItemsOfCountryIfRx(countryCode, limit)
        val roomAny = room.getItemsOfCountryRx(countryCode, limit)
        return concatFirstRx(true, remoteIf, roomAny)
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItemsRx(ts: List<Station>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    //region private
    private fun getRemoteItemsOfCountryIfRx(countryCode: String, limit: Long): Maybe<List<Station>> {
        return Maybe.create { emitter ->
            var result: List<Station>? = null
            if (mapper.isExpired(State.LOCAL, countryCode)) {
                result = remote.getItemsOfCountry(countryCode, limit)
            }
            if (emitter.isDisposed) return@create
            if (result.isNullOrEmpty()) {
                emitter.onError(EmptyException())
            } else {
                //extra work to save result
                room.putItems(result)
                mapper.commitStationExpiredTime(State.LOCAL, countryCode)
                emitter.onSuccess(result)
            }
        }
    }

    private fun getRemoteItemsOfTrendsIfRx(limit: Long): Maybe<List<Station>> {
        return Maybe.create { emitter ->
            var result: List<Station>? = null
            if (mapper.isExpired(State.TRENDS)) {
                result = remote.getItemsOfTrends(limit)
            }
            if (emitter.isDisposed) return@create
            if (result.isNullOrEmpty()) {
                emitter.onError(EmptyException())
            } else {
                //extra work to save result
                room.putItems(result)
                mapper.commitStationExpiredTime(State.TRENDS)
                emitter.onSuccess(result)
            }
        }
    }

    private fun getRemoteItemsOfPopularIfRx(limit: Long): Maybe<List<Station>> {
        return Maybe.create { emitter ->
            var result: List<Station>? = null
            if (mapper.isExpired(State.POPULAR)) {
                result = remote.getItemsOfPopular(limit)
            }
            if (emitter.isDisposed) return@create
            if (result.isNullOrEmpty()) {
                emitter.onError(EmptyException())
            } else {
                //extra work to save result
                room.putItems(result)
                mapper.commitStationExpiredTime(State.POPULAR)
                emitter.onSuccess(result)
            }
        }
    }
}