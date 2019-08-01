package com.dreampany.history.data.source.room

import com.dreampany.frame.data.model.ImageLink
import com.dreampany.frame.data.source.dao.ImageLinkDao
import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.history.data.enums.HistorySource
import com.dreampany.history.data.misc.ImageLinkMapper
import com.dreampany.history.data.source.api.ImageLinkDataSource
import io.reactivex.Maybe
import javax.inject.Singleton

/**
 * Created by Roman-372 on 7/30/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class RoomImageLinkDataSource(
    val mapper: ImageLinkMapper,
    val dao: ImageLinkDao
) : ImageLinkDataSource {
    override fun getItem(source: HistorySource, ref: String, url: String): ImageLink? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItem(t: ImageLink): Long {
        return dao.insertOrReplace(t)
    }

    override fun getItem(id: String): ImageLink? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemRx(source: HistorySource, ref: String, url: String): Maybe<ImageLink> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(t: ImageLink): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemRx(id: String): Maybe<ImageLink> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(source: HistorySource, ref: String): List<ImageLink>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isEmpty(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExists(t: ImageLink): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItems(ts: List<ImageLink>): List<Long>? {
        val result = mutableListOf<Long>()
        ts.forEach { result.add(putItem(it)) }
        return result
    }

    override fun deleteRx(t: ImageLink): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(): List<ImageLink>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(limit: Int): List<ImageLink>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(source: HistorySource, ref: String): Maybe<List<ImageLink>> {
        return dao.getItemsRx(ref)
    }

    override fun isEmptyRx(): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCountRx(): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExistsRx(t: ImageLink): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItemRx(t: ImageLink): Maybe<Long> {
        return dao.insertOrReplaceRx(t)
    }

    override fun putItemsRx(ts: List<ImageLink>): Maybe<List<Long>> {
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

    override fun delete(ts: List<ImageLink>): List<Long>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(ts: List<ImageLink>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(): Maybe<List<ImageLink>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(limit: Int): Maybe<List<ImageLink>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}