package com.dreampany.tools.data.source.remote

import com.dreampany.framework.misc.exceptions.EmptyException
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.api.radio.RadioStation
import com.dreampany.tools.data.mapper.StationMapper
import com.dreampany.tools.data.model.Station
import com.dreampany.tools.data.source.api.StationDataSource
import com.dreampany.tools.api.radio.StationService
import io.reactivex.Maybe
import timber.log.Timber

/**
 * Created by roman on 2019-10-11
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class RemoteStationDataSource
constructor(
    private val network: NetworkManager,
    private val mapper: StationMapper,
    private val service: StationService
) : StationDataSource {

    override fun getItemsOfTrends(limit: Long): List<Station>? {
        if (!network.hasInternet()) {
            return null
        }
        try {
            val response = service.getItemsOfTrends(limit).execute()
            if (response.isSuccessful) {
                val stations: List<RadioStation>? = response.body()
                val result = mapper.getItems(stations)
                return result
            }
        } catch (error: Throwable) {
            Timber.e(error)
        }
        return null
    }

    override fun getItemsOfTrendsRx(limit: Long): Maybe<List<Station>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsOfPopular(limit: Long): List<Station>? {
        if (!network.hasInternet()) {
            return null
        }
        try {
            val response = service.getItemsOfPopular(limit).execute()
            if (response.isSuccessful) {
                val stations: List<RadioStation>? = response.body()
                val result = mapper.getItems(stations)
                return result
            }
        } catch (error: Throwable) {
            Timber.e(error)
        }
        return null
    }

    override fun getItemsOfPopularRx(limit: Long): Maybe<List<Station>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsOfCountry(countryCode: String, limit: Long): List<Station>? {
        if (!network.hasInternet()) {
            return null
        }
        try {
            val response = service.getItemsOfCountry(countryCode, limit).execute()
            if (response.isSuccessful) {
                val stations: List<RadioStation>? = response.body()
                val result = mapper.getItems(stations)
                return result
            }
        } catch (error: Throwable) {
            Timber.e(error)
        }
        return null
    }

    override fun getItemsOfCountryRx(countryCode: String, limit: Long): Maybe<List<Station>> {
        return Maybe.create { emitter ->
            val result = getItemsOfCountry(countryCode, limit)
            if (emitter.isDisposed) return@create

            if (result.isNullOrEmpty()) {
                emitter.onError(EmptyException())
            } else {
                emitter.onSuccess(result)
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
}