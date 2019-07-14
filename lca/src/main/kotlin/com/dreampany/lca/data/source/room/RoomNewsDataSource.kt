package com.dreampany.lca.data.source.room

import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.lca.data.misc.NewsMapper
import com.dreampany.lca.data.misc.PriceMapper
import com.dreampany.lca.data.model.News
import com.dreampany.lca.data.source.api.NewsDataSource
import com.dreampany.lca.data.source.dao.NewsDao
import com.dreampany.lca.data.source.dao.PriceDao
import io.reactivex.Maybe
import java.util.concurrent.Callable

/**
 * Created by roman on 2019-07-14
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class RoomNewsDataSource constructor(
    val mapper: NewsMapper,
    val dao: NewsDao
) : NewsDataSource {

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

    override fun isExists(news: News): Boolean {
        return false
    }

    override fun isExistsRx(news: News): Maybe<Boolean> {
        return Maybe.empty()
    }

    override fun putItem(news: News): Long {
        return dao.insertOrReplace(news)
    }

    override fun putItemRx(news: News): Maybe<Long> {
        return Maybe.create { emitter ->
            val result = putItem(news)
            if (emitter.isDisposed) {
                return@create
            }
            if (result == -1L) {
                emitter.onError(EmptyException())
            } else {
                emitter.onSuccess(result)
            }
        }
    }

    override fun putItems(news: List<News>): List<Long>? {
        return dao.insertOrReplace(news)
    }

    override fun putItemsRx(news: List<News>): Maybe<List<Long>> {
        return Maybe.fromCallable { putItems(news) }
    }

    override fun delete(news: News): Int {
        return 0
    }

    override fun deleteRx(news: News): Maybe<Int> {
        return Maybe.empty()
    }

    override fun delete(news: List<News>): List<Long>? {
        return null
    }

    override fun deleteRx(news: List<News>): Maybe<List<Long>> {
        return Maybe.empty()
    }

    override fun getItem(id: String): News? {
        return null
    }

    override fun getItemRx(id: String): Maybe<News> {
        return Maybe.empty()
    }

    override fun getItems(): List<News>? {
        return null
    }

    override fun getItemsRx(): Maybe<List<News>> {
        return Maybe.empty()
    }

    override fun getItems(limit: Int): List<News>? {
        return dao.getItems(limit)
    }

    override fun getItemsRx(limit: Int): Maybe<List<News>> {
        return dao.getItemsRx(limit)
    }
}