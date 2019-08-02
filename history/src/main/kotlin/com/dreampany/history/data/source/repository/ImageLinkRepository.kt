package com.dreampany.history.data.source.repository

import com.dreampany.history.data.model.ImageLink
import com.dreampany.frame.data.source.repository.RepositoryKt
import com.dreampany.frame.misc.Remote
import com.dreampany.frame.misc.ResponseMapper
import com.dreampany.frame.misc.Room
import com.dreampany.frame.misc.RxMapper
import com.dreampany.history.data.enums.HistorySource
import com.dreampany.history.data.enums.LinkSource
import com.dreampany.history.data.source.api.ImageLinkDataSource
import io.reactivex.Maybe
import io.reactivex.internal.functions.Functions
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-07-30
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class ImageLinkRepository
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    @Room private val room: ImageLinkDataSource,
    @Remote private val remote: ImageLinkDataSource
) : RepositoryKt<String, ImageLink>(rx, rm), ImageLinkDataSource {

    override fun getItem(source: LinkSource, id: String): ImageLink? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItem(t: ImageLink): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(id: String): ImageLink? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemRx(source: LinkSource, id: String): Maybe<ImageLink> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(t: ImageLink): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemRx(id: String): Maybe<ImageLink> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(source: LinkSource, ref: String): List<ImageLink>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isEmpty(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExists(t: ImageLink): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItems(ts: List<ImageLink>): List<Long>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    override fun getItemsRx(source: LinkSource, ref: String): Maybe<List<ImageLink>> {
        val room = this.room.getItemsRx(source, ref)
        val remote = this.remote.getItemsRx(source, ref)
            .filter { !it.isNullOrEmpty() }
            .doOnSuccess {
                rx.compute(this.room.putItemsRx(it)).subscribe(
                    Functions.emptyConsumer<List<Long>>(),
                    Functions.emptyConsumer<Throwable>()
                )
            }
        return concatFirstRx(false, room, remote)
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItemsRx(ts: List<ImageLink>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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