package com.dreampany.lca.data.source.room

import com.dreampany.lca.data.enums.IcoStatus
import com.dreampany.lca.data.misc.IcoMapper
import com.dreampany.lca.data.model.Ico
import com.dreampany.lca.data.source.api.IcoDataSource
import com.dreampany.lca.data.source.dao.IcoDao
import io.reactivex.Maybe
import javax.inject.Singleton

/**
 * Created by roman on 2019-07-14
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class RoomIcoDataSource constructor(
    val mapper: IcoMapper,
    val dao: IcoDao
) : IcoDataSource{

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
        return Maybe.empty()
    }

    override fun isExists(ico: Ico): Boolean {
        return false
    }

    override fun isExistsRx(ico: Ico): Maybe<Boolean> {
        return Maybe.empty()
    }

    override fun putItem(ico: Ico): Long {
        return dao.insertOrReplace(ico)
    }

    override fun putItemRx(ico: Ico): Maybe<Long> {
        return Maybe.fromCallable { putItem(ico) }
    }

    override fun putItems(icos: List<Ico>): List<Long>? {
        return dao.insertOrReplace(icos)
    }

    override fun putItemsRx(icos: List<Ico>): Maybe<List<Long>> {
        return Maybe.fromCallable { putItems(icos) }
    }

    override fun delete(ico: Ico): Int {
        return 0
    }

    override fun deleteRx(ico: Ico): Maybe<Int> {
        return Maybe.empty()
    }

    override fun delete(icos: List<Ico>): List<Long>? {
        return null
    }

    override fun deleteRx(icos: List<Ico>): Maybe<List<Long>> {
        return Maybe.empty()
    }

    override fun getItem(id: String): Ico? {
        return null
    }

    override fun getItemRx(id: String): Maybe<Ico> {
        return Maybe.empty()
    }

    override fun getItems(): List<Ico>? {
        return null
    }

    override fun getItemsRx(): Maybe<List<Ico>> {
        return Maybe.empty()
    }

    override fun getItems(limit: Int): List<Ico>? {
        return null
    }

    override fun getItemsRx(limit: Int): Maybe<List<Ico>> {
        return Maybe.empty()
    }

    override fun getLiveItems(limit: Int): List<Ico>? {
        return null
    }

    override fun getLiveItemsRx(limit: Int): Maybe<List<Ico>> {
        return dao.getItemsRx(IcoStatus.LIVE.name, limit)
    }

    override fun getUpcomingItems(limit: Int): List<Ico>? {
        return null
    }

    override fun getUpcomingItemsRx(limit: Int): Maybe<List<Ico>> {
        return dao.getItemsRx(IcoStatus.UPCOMING.name, limit)
    }

    override fun getFinishedItems(limit: Int): List<Ico>? {
        return null
    }

    override fun getFinishedItemsRx(limit: Int): Maybe<List<Ico>> {
        return dao.getItemsRx(IcoStatus.FINISHED.name, limit)
    }
}