package com.dreampany.crypto.data.source.remote

import android.content.Context
import com.dreampany.crypto.api.misc.ApiConstants
import com.dreampany.crypto.api.remote.response.ArticlesResponse
import com.dreampany.crypto.api.remote.service.NewsApiService
import com.dreampany.crypto.data.model.Article
import com.dreampany.crypto.data.source.api.ArticleDataSource
import com.dreampany.crypto.data.source.mapper.NewsMapper
import com.dreampany.framework.misc.exts.isDebug
import com.dreampany.framework.misc.exts.value
import com.dreampany.framework.misc.func.Keys
import com.dreampany.framework.misc.func.Parser
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.network.manager.NetworkManager
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
            keys.setKeys(ApiConstants.NewsApi.API_KEY_ROMAN_BJIT)
        } else {
            keys.setKeys(ApiConstants.NewsApi.API_KEY_ROMAN_BJIT)
        }
    }

    private fun getHeader(key: String): Map<String, String> {
        val header = Maps.newHashMap<String, String>()
        /*header.put(
            ApiConstants.CoinMarketCap.ACCEPT,
            ApiConstants.CoinMarketCap.ACCEPT_JSON
        )*/
        header.put(ApiConstants.NewsApi.API_KEY, key)
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
                    getHeader(key),
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
                        code = response.body()?.code.value().toInt()
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