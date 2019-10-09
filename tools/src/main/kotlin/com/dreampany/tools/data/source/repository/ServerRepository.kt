package com.dreampany.tools.data.source.repository

import com.dreampany.framework.data.enums.Quality
import com.dreampany.framework.data.misc.StoreMapper
import com.dreampany.framework.data.source.repository.Repository
import com.dreampany.framework.data.source.repository.StoreRepository
import com.dreampany.framework.misc.Remote
import com.dreampany.framework.misc.ResponseMapper
import com.dreampany.framework.misc.Room
import com.dreampany.framework.misc.RxMapper
import com.dreampany.framework.misc.exception.EmptyException
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.misc.ServerMapper
import com.dreampany.tools.data.model.Server
import com.dreampany.tools.data.source.api.ServerDataSource
import io.reactivex.Maybe
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-10-07
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class ServerRepository
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    private val network: NetworkManager,
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepository,
    private val mapper: ServerMapper,
    @Room private val room: ServerDataSource,
    @Remote private val remote: ServerDataSource
) : Repository<String, Server>(rx, rm), ServerDataSource {

    override fun getRandomItem(): Server? {
        return null
    }

    override fun getRandomItemRx(): Maybe<Server> {
        val remoteIf = getRemoteRandomItemIfRx()
        val roomIf = getRoomRandomItemIfRx()
        return concatSingleFirstRx(remoteIf, roomIf)
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(): Maybe<List<Server>> {
        return remote.getItemsRx()
    }

    override fun getItems(limit: Long): List<Server>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(limit: Long): Maybe<List<Server>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // region private
    private fun getRemoteRandomItemIfRx(): Maybe<Server> {
        return Maybe.create { emitter ->
            var result: List<Server>? = null
            if (mapper.isServerExpired()) {
                result = remote.getItems()
            }
            if (emitter.isDisposed) return@create
            if (result.isNullOrEmpty()) {
                emitter.onError(EmptyException())
            } else {
                //extra work to save result
                room.putItems(result)
                mapper.commitServerExpiredTime()
                val random = getHighRandomItem(result)
                if (random == null) {
                    emitter.onError(EmptyException())
                } else {
                    emitter.onSuccess(random)
                }
            }
        }
    }

    private fun getRoomRandomItemIfRx(): Maybe<Server> {
        return Maybe.create { emitter ->
            var result: List<Server>? = room.getItems()
            if (emitter.isDisposed) return@create
            if (result.isNullOrEmpty()) {
                emitter.onError(EmptyException())
            } else {
                val random = getHighRandomItem(result)
                if (random == null) {
                    emitter.onError(EmptyException())
                } else {
                    emitter.onSuccess(random)
                }
            }
        }
    }

    private fun getHighRandomItem(list: List<Server>): Server? {
        val copy = mutableListOf<Server>()
        copy.addAll(list)
        val qualities = arrayListOf(Quality.HIGH, Quality.MEDIUM, Quality.LOW)
        for (quality in qualities) {
            val parts = arrayListOf<Server>()
            for (server in copy) {
                if (quality == server.quality) {
                    parts.add(server)
                }
            }
            if (!parts.isNullOrEmpty()) {
                return parts.random()
            }
            copy.removeAll(parts)
        }
        return null
    }
    // endregion
}