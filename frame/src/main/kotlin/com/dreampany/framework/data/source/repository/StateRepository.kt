/*
package com.dreampany.frame.extra.source.repository

import com.dreampany.frame.extra.model.State
import com.dreampany.frame.extra.source.api.StateDataSource
import com.dreampany.frame.misc.ResponseMapper
import com.dreampany.frame.misc.Room
import com.dreampany.frame.misc.RxMapper
import io.reactivex.Maybe
import javax.inject.Inject
import javax.inject.Singleton

*/
/**
 * Created by Roman-372 on 7/19/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 *//*

@Singleton
class StateRepository @Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    @Room private val room: StateDataSource
): Repository<String, State>(rx, rm), StateDataSource {

    override fun getCountById(id: String, type: String, subtype: String): Int {
        return room.getCountById(id, type, subtype)
    }

    override fun getCountByIdRx(id: String, type: String, subtype: String): Maybe<Int> {
        return Maybe.empty()
    }

    override fun isEmpty(): Boolean {
        return room.isEmpty()
    }

    override fun isEmptyRx(): Maybe<Boolean> {
        return Maybe.fromCallable({ this.isEmpty() })
    }

    override fun getCount(type: String, subtype: String, state: String): Int {
        return room.getCount(type, subtype, state)
    }

    override fun getCountById(id: String, type: String, subtype: String, state: String): Int {
        return room.getCountById(id, type, subtype, state)
    }

    override fun getCountRx(type: String, subtype: String, state: String): Maybe<Int> {
        return room.getCountRx(type, subtype, state)
    }

    override fun getCountRx(id: String, type: String, subtype: String, state: String): Maybe<Int> {
        return room.getCountRx(id, type, subtype, state)
    }

    override fun getItem(id: String, type: String, subtype: String, state: String): State? {
        return room.getItem(id, type, subtype, state)
    }

    override fun getItemRx(id: String, type: String, subtype: String, state: String): Maybe<State> {
        return room.getItemRx(id, type, subtype, state)
    }

    override fun getItemOrderByRx(
        type: String,
        subtype: String,
        state: String,
        from: Long,
        to: Long
    ): Maybe<State> {
        return room.getItemOrderByRx(type, subtype, state, from, to)
    }

    override fun getCount(): Int {
        return room.getCount()
    }

    override fun getCountRx(): Maybe<Int> {
        return room.getCountRx()
    }

    override fun getItems(type: String, subtype: String): List<State>? {
        return room.getItems(type, subtype)
    }

    override fun getItemOrderBy(type: String, subtype: String): State? {
        return null
    }

    override fun getItemNotStateOrderByRx(
        type: String,
        subtype: String,
        state: String
    ): Maybe<State> {
        return room.getItemNotStateOrderByRx(type, subtype, state)
    }

    override fun getItemOrderByRx(type: String, subtype: String): Maybe<State> {
        return room.getItemOrderByRx(type, subtype)
    }

    override fun getItemsRx(type: String, subtype: String): Maybe<List<State>> {
        return room.getItemsRx(type, subtype)
    }

    override fun getItemsOrderBy(
        type: String,
        subtype: String,
        from: Long,
        to: Long
    ): List<State>? {
        return room.getItemsOrderBy(type, subtype, from, to)
    }

    override fun getItemsOrderByRx(
        type: String,
        subtype: String,
        from: Long,
        to: Long
    ): Maybe<List<State>> {
        return room.getItemsOrderByRx(type, subtype, from, to)
    }

    override fun getItem(type: String, subtype: String, state: String): State? {
        return room.getItem(type, subtype, state)
    }

    override fun getItemRx(type: String, subtype: String, state: String): Maybe<State> {
        return room.getItemRx(type, subtype, state)
    }

    override fun getItems(type: String, subtype: String, state: String): List<State>? {
        return room.getItems(type, subtype, state)
    }

    override fun getItemsRx(type: String, subtype: String, state: String): Maybe<List<State>> {
        return room.getItemsRx(type, subtype, state)
    }

    override fun getItemsOrderBy(type: String, subtype: String): List<State>? {
        return room.getItemsOrderBy(type, subtype)
    }

    override fun getItemsOrderByRx(type: String, subtype: String): Maybe<List<State>> {
        return room.getItemsOrderByRx(type, subtype)
    }

    override fun getItemsOrderBy(type: String, subtype: String, state: String): List<State>? {
        return room.getItemsOrderBy(type, subtype, state)
    }

    override fun getItemsOrderByRx(
        type: String,
        subtype: String,
        state: String
    ): Maybe<List<State>> {
        return room.getItemsOrderByRx(type, subtype, state)
    }

    override fun getItemsOrderBy(type: String, subtype: String, limit: Int): List<State>? {
        return null
    }

    override fun getItemsOrderByRx(type: String, subtype: String, limit: Int): Maybe<List<State>> {
        return room.getItemsOrderByRx(type, subtype, limit)
    }

    override fun getItemsOrderBy(
        type: String,
        subtype: String,
        state: String,
        limit: Int
    ): List<State>? {
        return room.getItemsOrderBy(type, subtype, state, limit)
    }

    override fun getItemsOrderByRx(
        type: String,
        subtype: String,
        state: String,
        limit: Int
    ): Maybe<List<State>> {
        return room.getItemsOrderByRx(type, subtype, state, limit)
    }

    override fun getItems(type: String, subtype: String, limit: Int): List<State>? {
        return room.getItems(type, subtype, limit)
    }

    override fun getItemsRx(type: String, subtype: String, limit: Int): Maybe<List<State>> {
        return room.getItemsRx(type, subtype, limit)
    }

    override fun isExists(state: State): Boolean {
        return room.isExists(state)
    }

    override fun isExistsRx(state: State): Maybe<Boolean> {
        return room.isExistsRx(state)
    }

    override fun putItem(state: State): Long {
        return room.putItem(state)
    }

    override fun putItemRx(state: State): Maybe<Long> {
        return room.putItemRx(state)
    }

    override fun putItems(states: List<State>): List<Long>? {
        return room.putItems(states)
    }

    override fun putItemsRx(states: List<State>): Maybe<List<Long>> {
        return room.putItemsRx(states)
    }

    override fun delete(state: State): Int {
        return room.delete(state)
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
        return room.getItems()
    }

    override fun getItemsRx(): Maybe<List<State>> {
        return room.getItemsRx()
    }

    override fun getItems(limit: Int): List<State>? {
        return room.getItems(limit)
    }

    override fun getItemsRx(limit: Int): Maybe<List<State>> {
        return room.getItemsRx(limit)
    }
}*/
