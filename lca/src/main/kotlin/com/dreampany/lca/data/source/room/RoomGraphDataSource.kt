package com.dreampany.lca.data.source.room

import com.dreampany.lca.data.misc.GraphMapper
import com.dreampany.lca.data.model.Graph
import com.dreampany.lca.data.source.api.GraphDataSource
import com.dreampany.lca.data.source.dao.GraphDao
import io.reactivex.Maybe
import javax.inject.Singleton

/**
 * Created by roman on 2019-07-14
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class RoomGraphDataSource constructor(
    val mapper: GraphMapper,
    val dao: GraphDao
) : GraphDataSource {

    override fun isEmpty(): Boolean {
        return getCount() == 0
    }

    override fun isEmptyRx(): Maybe<Boolean> {
        return Maybe.fromCallable( { this.isEmpty() })
    }

    override fun getCount(): Int {
        return dao.count
    }

    override fun getCountRx(): Maybe<Int> {
        return Maybe.empty()
    }

    override fun isExists(graph: Graph): Boolean {
        return false
    }

    override fun isExistsRx(graph: Graph): Maybe<Boolean> {
        return Maybe.empty()
    }

    override fun putItem(graph: Graph): Long {
        return 0
    }

    override fun putItemRx(graph: Graph): Maybe<Long> {
        return Maybe.fromCallable { putItem(graph) }
    }

    override  fun putItems(graphs: List<Graph>): List<Long>? {
        return null
    }

    override  fun putItemsRx(graphs: List<Graph>): Maybe<List<Long>> {
        return Maybe.empty()
    }

    override fun delete(graph: Graph): Int {
        return 0
    }

    override fun deleteRx(graph: Graph): Maybe<Int> {
        return Maybe.empty()
    }

    override fun delete(graphs: List<Graph>): List<Long>? {
        return null
    }

    override fun deleteRx(graphs: List<Graph>): Maybe<List<Long>> {
        return Maybe.empty()
    }

    override fun getItem(id: String): Graph? {
        return null
    }

    override fun getItemRx(id: String): Maybe<Graph> {
        return Maybe.empty()
    }

    override fun getItems(): List<Graph>? {
        return null
    }

    override fun getItemsRx(): Maybe<List<Graph>> {
        return Maybe.empty()
    }

    override fun getItems(limit: Int): List<Graph>? {
        return null
    }

    override fun getItemsRx(limit: Int): Maybe<List<Graph>> {
        return Maybe.empty()
    }

    override fun getItem(slug: String, startTime: Long, endTime: Long): Graph? {
        return null
    }

    override fun getItemRx(slug: String, startTime: Long, endTime: Long): Maybe<Graph> {
        return dao.getItemRx(slug, startTime, endTime)
    }
}