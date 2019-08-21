/*
package com.dreampany.frame.data.source.room

import com.dreampany.frame.data.misc.StateMapper
import com.dreampany.frame.data.model.State
import com.dreampany.frame.data.source.api.StateDataSource
import com.dreampany.frame.data.source.dao.StateDao
import io.reactivex.Maybe
import javax.inject.Singleton

*/
/**
 * Created by Roman-372 on 7/19/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 *//*

@Singleton
class RoomStateDataSource(val mapper: StateMapper, val dao: StateDao) : StateDataSource {

    override fun getCountById(id: String, type: String, subtype: String): Int {
        return dao.getCountById(id, type, subtype)
    }

    override fun getCountByIdRx(id: String, type: String, subtype: String): Maybe<Int> {
        return Maybe.empty()
    }

    override fun isEmpty(): Boolean {
        return getCount() == 0
    }

    override fun isEmptyRx(): Maybe<Boolean> {
        return Maybe.fromCallable({ this.isEmpty() })
    }

    override fun getCount(): Int {
        return dao.count
    }

    override fun getCountRx(): Maybe<Int> {
        return dao.countRx
    }

    override fun getCount(type: String, subtype: String, state: String): Int {
        return dao.getCount(type, subtype, state)
    }

    override fun getCountRx(type: String, subtype: String, state: String): Maybe<Int> {
        return dao.getCountRx(type, subtype, state)
    }

    override fun getCountById(id: String, type: String, subtype: String, state: String): Int {
        return dao.getCount(id, type, subtype, state)
    }

    override fun getCountRx(id: String, type: String, subtype: String, state: String): Maybe<Int> {
        return dao.getCountRx(id, type, subtype, state)
    }

    override fun getItem(id: String, type: String, subtype: String, state: String): State? {
        var item = mapper.getItem(id, type, subtype, state)
        if (item == null) {
            item = dao.getItem(id, type, subtype, state)
        }
        if (item == null) {
            item = State(id, type, subtype, state)
        }
        mapper.putItem(item)
        return item
    }

    override fun getItemRx(id: String, type: String, subtype: String, state: String): Maybe<State> {
        return if (mapper.isExists(id, type, subtype, state)) {
            Maybe.fromCallable { mapper.getItem(id, type, subtype, state) }
        } else dao.getItemRx(id, type, subtype, state)
            .doOnSuccess({ mapper.putItem(it) })
    }

    override fun getItemOrderByRx(
        type: String,
        subtype: String,
        state: String,
        from: Long,
        to: Long
    ): Maybe<State> {
        return dao.getItemOrderByRx(type, subtype, state, from, to)
    }

    override fun getItems(type: String, subtype: String): List<State>? {
        return dao.getItems(type, subtype)
    }


    override fun getItemOrderBy(type: String, subtype: String): State? {
        return null
    }

    override fun getItemNotStateOrderByRx(
        type: String,
        subtype: String,
        state: String
    ): Maybe<State> {
        return dao.getItemNotStateOrderByRx(type, subtype, state)
    }

    override fun getItemOrderByRx(type: String, subtype: String): Maybe<State> {
        return Maybe.empty()
    }

    override fun getItemsRx(type: String, subtype: String): Maybe<List<State>> {
        return dao.getItemsRx(type, subtype)
    }

    override fun getItemsOrderBy(
        type: String,
        subtype: String,
        from: Long,
        to: Long
    ): List<State>? {
        return dao.getItemsOrderBy(type, subtype, from, to)
    }

    override fun getItemsOrderByRx(
        type: String,
        subtype: String,
        from: Long,
        to: Long
    ): Maybe<List<State>> {
        return dao.getItemsOrderByRx(type, subtype, from, to)
    }

    override fun getItem(type: String, subtype: String, state: String): State? {
        return dao.getItem(type, subtype, state)
    }

    override fun getItemRx(type: String, subtype: String, state: String): Maybe<State> {
        return dao.getItemRx(type, subtype, state)
    }

    override fun getItems(type: String, subtype: String, state: String): List<State>? {
        return dao.getItemsWithoutId(type, subtype, state)
    }

    override fun getItemsRx(type: String, subtype: String, state: String): Maybe<List<State>> {
        return dao.getItemsRx(type, subtype, state)
    }

    override fun getItemsOrderBy(type: String, subtype: String): List<State>? {
        return null
    }

    override fun getItemsOrderByRx(type: String, subtype: String): Maybe<List<State>> {
        return dao.getItemsOrderByRx(type, subtype)
    }

    override fun getItemsOrderBy(type: String, subtype: String, state: String): List<State>? {
        return dao.getItemsOrderBy(type, subtype, state)
    }

    override fun getItemsOrderByRx(
        type: String,
        subtype: String,
        state: String
    ): Maybe<List<State>> {
        return dao.getItemsOrderByRx(type, subtype, state)
    }

    override fun getItemsOrderBy(type: String, subtype: String, limit: Int): List<State>? {
        return null
    }

    override fun getItemsOrderByRx(type: String, subtype: String, limit: Int): Maybe<List<State>> {
        return dao.getItemsOrderByRx(type, subtype, limit)
    }

    override fun getItemsOrderBy(
        type: String,
        subtype: String,
        state: String,
        limit: Int
    ): List<State>? {
        return dao.getItemsOrderBy(type, subtype, state, limit)
    }

    override fun getItemsOrderByRx(
        type: String,
        subtype: String,
        state: String,
        limit: Int
    ): Maybe<List<State>> {
        return dao.getItemsOrderByRx(type, subtype, state, limit)
    }

    override fun getItems(type: String, subtype: String, limit: Int): List<State>? {
        return dao.getItems(type, subtype, limit)
    }

    override fun getItemsRx(type: String, subtype: String, limit: Int): Maybe<List<State>> {
        return dao.getItemsRx(type, subtype, limit)
    }

    override fun isExists(state: State): Boolean {
        return dao.getCount(state.id, state.type, state.subtype, state.state) > 0
    }

    override fun isExistsRx(state: State): Maybe<Boolean> {
        return Maybe.fromCallable { isExists(state) }
    }

    override fun putItem(state: State): Long {
        return dao.insertOrReplace(state)
    }

    override fun putItemRx(state: State): Maybe<Long> {
        return Maybe.fromCallable { putItem(state) }
    }

    override fun putItems(states: List<State>): List<Long>? {
        return dao.insertOrReplace(states)
    }

    override fun putItemsRx(states: List<State>): Maybe<List<Long>> {
        return Maybe.fromCallable { putItems(states) }
    }

    override fun delete(state: State): Int {
        return dao.delete(state)
    }

    override fun deleteRx(state: State): Maybe<Int> {
        return Maybe.empty()
    }

    override fun delete(states: List<State>): List<Long>? {
        return null
    }

    override fun deleteRx(states: List<State>): Maybe<List<Long>> {
        return Maybe.empty()
    }

    override fun getItem(id: String): State? {
        return null
    }

    override fun getItemRx(id: String): Maybe<State> {
        return Maybe.empty()
    }

    override fun getItems(): List<State>? {
        return dao.items
    }

    override fun getItemsRx(): Maybe<List<State>> {
        return dao.itemsRx
    }

    override fun getItems(limit: Int): List<State>? {
        return dao.getItems(limit)
    }

    override fun getItemsRx(limit: Int): Maybe<List<State>> {
        return dao.getItemsRx(limit)
    }
}*/
