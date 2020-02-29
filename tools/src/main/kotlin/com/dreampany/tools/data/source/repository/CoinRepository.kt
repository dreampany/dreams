package com.dreampany.tools.data.source.repository

import com.dreampany.framework.data.misc.StoreMapper
import com.dreampany.framework.data.source.repository.Repository
import com.dreampany.framework.data.source.repository.StoreRepository
import com.dreampany.framework.injector.annote.Database
import com.dreampany.framework.injector.annote.Remote
import com.dreampany.framework.injector.annote.Room
import com.dreampany.framework.misc.ResponseMapper
import com.dreampany.framework.misc.RxMapper
import com.dreampany.framework.misc.exception.EmptyException
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.enums.CoinSort
import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.data.enums.Order
import com.dreampany.tools.data.mapper.crypto.CoinMapper
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.source.api.crypto.CoinDataSource
import io.reactivex.Maybe
import io.reactivex.functions.Consumer
import io.reactivex.internal.functions.Functions
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-11-11
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class CoinRepository
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    private val network: NetworkManager,
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepository,
    private val mapper: CoinMapper,
    @Room private val room: CoinDataSource,
    @Database private val database: CoinDataSource,
    @Remote private val remote: CoinDataSource
) : Repository<String, Coin>(rx, rm),
    CoinDataSource {

    override fun getItem(currency: Currency, id: String): Coin? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(
        currency: Currency,
        sort: CoinSort,
        order: Order,
        start: Long,
        limit: Long
    ): List<Coin>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(
        currency: Currency,
        sort: CoinSort,
        order: Order,
        start: Long,
        limit: Long
    ): Maybe<List<Coin>> {
        val remoteIf = getRemoteItemsIfRx(currency, sort, order, start, limit)
        val roomAny = room.getItemsRx(currency, sort, order, start, limit)
        return concatFirstRx(true, remoteIf, roomAny)
    }

    override fun getItems(currency: Currency, ids: List<String>): List<Coin>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(currency: Currency, ids: List<String>): Maybe<List<Coin>> {
        return Maybe.create { emitter ->
            val result = arrayListOf<Coin>()
            val roomIds = arrayListOf<String>()
            val remoteIds = arrayListOf<String>()
            ids.forEach { id ->
                if (mapper.isExpired(currency, id)) {
                    remoteIds.add(id)
                } else {
                    roomIds.add(id)
                }

                /*var coin: Coin? = null
                 if (mapper.isExpired(currency, id)) {
                     coin = remote.getItem(id)
                     coin?.run {
                         room.putItem(this)
                         mapper.commitExpire(currency, id)
                     }
                 }
                 if (coin == null) {
                     coin = room.getItem(id)
                 }
                 coin?.run {
                     result.add(this)
                 }*/
            }
            val roomResult = room.getItems(currency, roomIds)
            val remoteResult = remote.getItems(currency, remoteIds)
            roomResult?.run {
                result.addAll(this)
                room.putItems(this)
                forEach {
                    mapper.commitExpire(currency, it.id)
                }
            }
            remoteResult?.run {
                result.addAll(this)
            }
            if (emitter.isDisposed) return@create
            if (result.isNullOrEmpty()) {
                emitter.onError(EmptyException())
            } else {
                emitter.onSuccess(result)
            }
        }
    }

    override fun getItems(): List<Coin>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(limit: Long): List<Coin>? {
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

    override fun getItemRx(currency: Currency, id: String): Maybe<Coin> {
        val databaseIf: Maybe<Coin> = getDatabaseItemIfRx(currency, id)
        val remoteIf: Maybe<Coin> = getRemoteItemIfRx(currency, id)
        val room: Maybe<Coin> = room.getItemRx(currency, id)
        return concatSingleFirstRx(databaseIf, remoteIf, room)
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

    private fun getDatabaseItemIfRx(currency: Currency, id: String): Maybe<Coin> {
        val maybe: Maybe<Coin> =
            if (mapper.isExpired(currency, id)) database.getItemRx(currency, id)
            else Maybe.empty<Coin>()
        return concatSingleSuccess(maybe, Consumer {
            rx.compute(room.putItemRx(it)).subscribe(
                Functions.emptyConsumer(),
                Functions.emptyConsumer()
            )
            mapper.commitExpire(currency, it.id, it.getLastUpdated())
        })
    }

    private fun getRemoteItemIfRx(currency: Currency, id: String): Maybe<Coin> {
        val maybe: Maybe<Coin> =
            if (mapper.isExpired(currency, id)) remote.getItemRx(currency, id)
            else Maybe.empty<Coin>()
        return concatSingleSuccess(maybe, Consumer {
            rx.compute(room.putItemRx(it)).subscribe(
                Functions.emptyConsumer(),
                Functions.emptyConsumer()
            )
            rx.compute(database.putItemRx(it)).subscribe(
                Functions.emptyConsumer(),
                Functions.emptyConsumer()
            )
            mapper.commitExpire(currency, it.id, it.getLastUpdated())
        })
    }

    private fun getRemoteItemsIfRx(
        currency: Currency,
        sort: CoinSort,
        order: Order,
        start: Long,
        limit: Long
    ): Maybe<List<Coin>> {
        return Maybe.create { emitter ->
            var result: List<Coin>? = null
            if (mapper.isExpired(currency, sort, order, start)) {
                result = remote.getItems(currency, sort, order, start, limit)
            }
            if (emitter.isDisposed) return@create
            if (result.isNullOrEmpty()) {
                emitter.onError(EmptyException())
            } else {
                val res = room.putItems(result)
                Timber.v("Room Loading %d - %s", result.size, res?.size)
                mapper.commitExpire(currency, sort, order, start)
                emitter.onSuccess(result)
            }
        }
    }

}