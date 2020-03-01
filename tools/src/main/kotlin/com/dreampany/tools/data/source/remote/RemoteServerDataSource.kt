package com.dreampany.tools.data.source.remote

import android.content.Context
import com.dreampany.framework.data.source.api.RemoteService
import com.dreampany.framework.misc.exceptions.EmptyException
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.mapper.ServerMapper
import com.dreampany.tools.data.model.Server
import com.dreampany.tools.data.source.api.ServerDataSource
import com.dreampany.tools.misc.Constants
import io.reactivex.Maybe
import timber.log.Timber
import java.io.File

/**
 * Created by roman on 2019-10-06
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class RemoteServerDataSource
constructor(
    private val context: Context,
    private val network: NetworkManager,
    private val mapper: ServerMapper,
    private val service: RemoteService
) : ServerDataSource {
    override fun deleteAll() {

    }

    override fun getRandomItem(): Server? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRandomItemRx(): Maybe<Server> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getServersRx(countryCode: String): Maybe<List<Server>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    override fun isExists(t: Server): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExistsRx(t: Server): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItem(t: Server): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItemRx(t: Server): Maybe<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItems(ts: List<Server>): List<Long>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItemsRx(ts: List<Server>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(t: Server): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(t: Server): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(ts: List<Server>): List<Long>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(ts: List<Server>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(id: String): Server? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemRx(id: String): Maybe<Server> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(): List<Server>? {
        if (!network.hasInternet()) {
            return null
        }
        try {
            val response = service.get(Constants.VpnGate.URL).execute()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val tempUrl = context.cacheDir.path.plus(File.separator)
                        .plus(Constants.VpnGate.FILE_NAME)
                    val servers = mapper.getItems(body, tempUrl)
                    return servers
                }
            }
        } catch (error: Throwable) {
            Timber.e(error)
        }
        return null
    }

    override fun getItemsRx(): Maybe<List<Server>> {
        return Maybe.create { emitter ->
            val result = getItems()
            if (emitter.isDisposed) return@create

            if (result.isNullOrEmpty()) {
                emitter.onError(EmptyException())
            } else {
                emitter.onSuccess(result)
            }
        }
    }

    override fun getItems(limit: Long): List<Server>? {
        if (!network.hasInternet()) {
            return null
        }
        try {
            val response = service.get(Constants.VpnGate.URL).execute()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val tempUrl = context.cacheDir.path.plus(File.separator)
                        .plus(Constants.VpnGate.FILE_NAME)
                    val servers = mapper.getItems(body, tempUrl, limit)
                    return servers
                }
            }
        } catch (error: Throwable) {
            Timber.e(error)
        }
        return null
    }

    override fun getItemsRx(limit: Long): Maybe<List<Server>> {
        return Maybe.create { emitter ->
            val result = getItems(limit)
            if (emitter.isDisposed) return@create

            if (result.isNullOrEmpty()) {
                emitter.onError(EmptyException())
            } else {
                emitter.onSuccess(result)
            }
        }
    }


}