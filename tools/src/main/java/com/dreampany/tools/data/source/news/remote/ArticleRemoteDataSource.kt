package com.dreampany.tools.data.source.new.remote

import android.content.Context
import com.dreampany.framework.misc.exts.isDebug
import com.dreampany.framework.misc.exts.value
import com.dreampany.framework.misc.func.Keys
import com.dreampany.framework.misc.func.Parser
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.api.news.remote.response.ArticlesResponse
import com.dreampany.tools.api.news.remote.service.NewsApiService
import com.dreampany.tools.data.model.news.Article
import com.dreampany.tools.data.source.news.api.ArticleDataSource
import com.dreampany.tools.data.source.news.mapper.NewsMapper
import com.dreampany.tools.misc.constants.Constants
import com.google.common.collect.Maps
import retrofit2.Response
import java.net.UnknownHostException

/**
 * Created by roman on 3/21/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ArticleRemoteDataSource
constructor(
    private val context: Context,
    private val network: NetworkManager,
    private val parser: Parser,
    private val keys: Keys,
    private val mapper: NewsMapper,
    private val service: NewsApiService
) : ArticleDataSource {

    init {
        if (context.isDebug) {
            keys.setKeys(Constants.Apis.NewsApi.API_KEY_ROMAN_BJIT)
        } else {
            keys.setKeys(Constants.Apis.NewsApi.API_KEY_ROMAN_BJIT)
        }
    }

    private val String.header: Map<String, String>
        get() {
            val header = Maps.newHashMap<String, String>()
            header.put(
                Constants.Apis.CoinMarketCap.ACCEPT,
                Constants.Apis.CoinMarketCap.ACCEPT_JSON
            )
            header.put(Constants.Apis.NewsApi.API_KEY, this)
            return header
        }

    override suspend fun isFavorite(input: Article): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFavorite(input: Article): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getFavoriteArticles(): List<Article>? {
        TODO("Not yet implemented")
    }

    override suspend fun put(input: Article): Long {
        TODO("Not yet implemented")
    }

    override suspend fun put(inputs: List<Article>): List<Long>? {
        TODO("Not yet implemented")
    }

    override suspend fun get(id: String): Article? {
        TODO("Not yet implemented")
    }

    override suspend fun gets(): List<Article>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun gets(
        query: String,
        language: String,
        offset: Long,
        limit: Long
    ): List<Article>? {
        for (index in 0..keys.length) {
            try {
                val key = keys.nextKey ?: continue
                val response: Response<ArticlesResponse> = service.getEverything(
                    key.header,
                    query,
                    language,
                    offset, //Coin Market Cap start from 1 - IntRange
                    limit
                ).execute()
                if (response.isSuccessful) {
                    val data = response.body()?.articles ?: return null
                    return mapper.getItems(data)
                } else {
                    throw SmartError(
                        message = response.body()?.message,
                        code = response.body()?.code.value.toInt()
                    )
                    /*val error = parser.parseError(response, NewsResponse::class)
                    throw SmartError(
                        message = error?.status?.errorMessage,
                        code = error?.status?.errorCode
                    )*/
                }
            } catch (error: Throwable) {
                if (error is SmartError) throw error
                if (error is UnknownHostException) throw SmartError(
                    message = error.message,
                    error = error
                )
                keys.randomForwardKey()
            }
        }
        throw SmartError()
    }

    @Throws
    override suspend fun getsByCountry(country: String, offset: Long, limit: Long): List<Article>? {
        for (index in 0..keys.length) {
            try {
                val key = keys.nextKey ?: continue
                val response: Response<ArticlesResponse> = service.getHeadlinesByCountry(
                    key.header,
                    country,
                    offset,
                    limit
                ).execute()
                if (response.isSuccessful) {
                    val data = response.body()?.articles ?: return null
                    return mapper.getItems(data)
                } else {
                    throw SmartError(
                        message = response.body()?.message,
                        code = response.body()?.code.value.toInt()
                    )
                    /*val error = parser.parseError(response, NewsResponse::class)
                    throw SmartError(
                        message = error?.status?.errorMessage,
                        code = error?.status?.errorCode
                    )*/
                }
            } catch (error: Throwable) {
                if (error is SmartError) throw error
                if (error is UnknownHostException) throw SmartError(
                    message = error.message,
                    error = error
                )
                keys.randomForwardKey()
            }
        }
        throw SmartError()
    }

    @Throws
    override suspend fun getsByCategory(
        category: String,
        offset: Long,
        limit: Long
    ): List<Article>? {
        for (index in 0..keys.length) {
            try {
                val key = keys.nextKey ?: continue
                val response: Response<ArticlesResponse> = service.getHeadlinesByCategory(
                    key.header,
                    category,
                    offset, //Coin Market Cap start from 1 - IntRange
                    limit
                ).execute()
                if (response.isSuccessful) {
                    val data = response.body()?.articles ?: return null
                    return mapper.getItems(data)
                } else {
                    throw SmartError(
                        message = response.body()?.message,
                        code = response.body()?.code.value.toInt()
                    )
                    /*val error = parser.parseError(response, NewsResponse::class)
                    throw SmartError(
                        message = error?.status?.errorMessage,
                        code = error?.status?.errorCode
                    )*/
                }
            } catch (error: Throwable) {
                if (error is SmartError) throw error
                if (error is UnknownHostException) throw SmartError(
                    message = error.message,
                    error = error
                )
                keys.randomForwardKey()
            }
        }
        throw SmartError()
    }
}