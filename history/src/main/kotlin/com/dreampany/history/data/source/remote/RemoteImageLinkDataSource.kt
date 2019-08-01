package com.dreampany.history.data.source.remote

import com.dreampany.history.data.enums.LinkSource
import com.dreampany.history.data.misc.ImageLinkMapper
import com.dreampany.history.data.model.ImageLink
import com.dreampany.history.data.source.api.ImageLinkDataSource
import com.dreampany.network.manager.NetworkManager
import io.reactivex.Maybe
import org.jsoup.nodes.Element
import javax.inject.Singleton

/**
 * Created by roman on 2019-07-30
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class RemoteImageLinkDataSource(
    val network: NetworkManager,
    val mapper: ImageLinkMapper,
    val parser: ImageParser
) : ImageLinkDataSource {
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
        if (network.isObserving() && !network.hasInternet()) {
            return null
        }
        val images = parser.parse(ref)
        return getItems(source, ref, images)
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
        return Maybe.create { emitter ->
            val items = getItems(source, ref)
            if (emitter.isDisposed) {
                return@create
            }
            if (items.isNullOrEmpty()) {
                emitter.onComplete()
            } else {
                emitter.onSuccess(items)
            }
        }
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

    /* private api */
    private fun getItems(source: LinkSource, ref: String, inputs: List<Element>?): List<ImageLink>? {
        if (inputs.isNullOrEmpty()) {
            return null
        }
        val links = mutableListOf<ImageLink>()
        inputs.forEach { element ->
            mapper.toItem(source, ref, element)?.run {
                links.add(this)
            }
        }
        return links
    }
}