package com.dreampany.frame.data.source.room

import com.dreampany.frame.data.enums.State
import com.dreampany.frame.data.enums.Subtype
import com.dreampany.frame.data.enums.Type
import com.dreampany.frame.data.misc.StoreMapper
import com.dreampany.frame.data.model.Store
import com.dreampany.frame.data.source.api.StoreDataSource
import com.dreampany.frame.data.source.dao.StoreDao
import com.dreampany.frame.misc.exception.EmptyException
import io.reactivex.Maybe
import javax.inject.Singleton

/**
 * Created by roman on 2019-07-25
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class RoomStoreDataSource
constructor(
    private val mapper: StoreMapper,
    private val dao: StoreDao
) : StoreDataSource {

    override fun getItem(type: Type, subtype: Subtype, state: State): Store? {
        return dao.getItem(type.name, subtype.name, state.name)
    }

    override fun getItems(type: Type, subtype: Subtype, state: State): List<Store>? {
        return dao.getItems(type.name, subtype.name, state.name)
    }

    override fun getItemsRx(type: Type, subtype: Subtype, state: State): Maybe<List<Store>> {
        return dao.getItemsRx(type.name, subtype.name, state.name)
    }

    override fun getCount(id: String, type: Type, subtype: Subtype, state: State): Int {
        return dao.getCount(id, type.name, subtype.name, state.name)
    }

    override fun isExists(id: String, type: Type, subtype: Subtype, state: State): Boolean {
        return dao.getCount(id, type.name, subtype.name, state.name) > 0
    }

    override fun isExistsRx(
        id: String,
        type: Type,
        subtype: Subtype,
        state: State
    ): Maybe<Boolean> {
        return Maybe.create { emitter ->
            val result = isExists(id, type, subtype, state)
            if (emitter.isDisposed) {
                return@create
            }
            emitter.onSuccess(result)
        }
    }

    override fun getCount(id: String, type: Type, subtype: Subtype): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCountRx(id: String, type: Type, subtype: Subtype): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCountRx(): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCountByType(type: Type, subtype: Subtype, state: State): Int {
        return dao.getCountByType(type.name, subtype.name, state.name)
    }

    override fun getCountByTypeRx(type: Type, subtype: Subtype, state: State): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(id: String, type: Type, subtype: Subtype, state: State): Store? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItem(t: Store): Long {
        return dao.insertOrReplace(t)
    }

    override fun putItems(ts: List<Store>): List<Long>? {
        return dao.insertOrReplace(ts)
    }

    override fun delete(t: Store): Int {
        return dao.delete(t)
    }

    override fun deleteRx(t: Store): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(id: String): Store? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemRx(id: String, type: Type, subtype: Subtype, state: State): Maybe<Store> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isEmpty(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isEmptyRx(): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExists(t: Store): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExistsRx(t: Store): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItemRx(t: Store): Maybe<Long> {
        return dao.insertOrReplaceRx(t)
    }

    override fun putItemsRx(ts: List<Store>): Maybe<List<Long>> {
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

    override fun delete(ts: List<Store>): List<Long>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(ts: List<Store>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemRx(id: String): Maybe<Store> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(): List<Store>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(): Maybe<List<Store>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(limit: Int): List<Store>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(limit: Int): Maybe<List<Store>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}