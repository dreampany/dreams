package com.dreampany.news.data.source.room

import com.dreampany.news.data.model.Article
import com.dreampany.news.data.source.api.ArticleDataSource
import com.dreampany.news.data.source.mapper.ArticleMapper
import com.dreampany.news.data.source.room.dao.ArticleDao


/**
 * Created by roman on 9/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ArticleRoomDataSource(
    private val mapper: ArticleMapper,
    private val dao: ArticleDao
): ArticleDataSource {
    override suspend fun isFavorite(input: Article): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFavorite(input: Article): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getFavoriteArticles(): List<Article>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun put(input: Article): Long {
        mapper.add(input)
        return dao.insertOrReplace(input)
    }

    @Throws
    override suspend fun put(inputs: List<Article>): List<Long>? {
        val result = arrayListOf<Long>()
        inputs.forEach { result.add(put(it)) }
        return result
    }

    override suspend fun get(id: String): Article? {
        TODO("Not yet implemented")
    }

    override suspend fun gets(): List<Article>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun getsOfQuery(
        query: String,
        language: String,
        offset: Long,
        limit: Long
    ): List<Article>? = mapper.getItems(query, language, offset, limit, this)

    override suspend fun getsByCountry(country: String, offset: Long, limit: Long): List<Article>? {
        TODO("Not yet implemented")
    }

    override suspend fun getsByCategory(
        category: String,
        offset: Long,
        limit: Long
    ): List<Article>? {
        TODO("Not yet implemented")
    }
}