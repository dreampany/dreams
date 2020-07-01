package com.dreampany.tube.data.source.remote

import android.content.Context
import com.dreampany.framework.misc.exts.isDebug
import com.dreampany.framework.misc.func.Keys
import com.dreampany.framework.misc.func.Parser
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tube.api.misc.ApiConstants
import com.dreampany.tube.api.remote.service.YoutubeService
import com.dreampany.tube.data.model.Category
import com.dreampany.tube.data.source.api.CategoryDataSource
import com.dreampany.tube.data.source.mapper.CategoryMapper
import java.net.UnknownHostException

/**
 * Created by roman on 30/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CategoryRemoteDataSource(
    private val context: Context,
    private val network: NetworkManager,
    private val parser: Parser,
    private val keys: Keys,
    private val mapper: CategoryMapper,
    private val service: YoutubeService
) : CategoryDataSource {

    init {
        keys.setKeys(ApiConstants.Youtube.API_KEY_ROMAN_BJIT)
        if (!context.isDebug) {
            keys.setKeys(ApiConstants.Youtube.API_KEY_DREAMPANY_MAIL)
        }
    }

    override suspend fun isFavorite(input: Category): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFavorite(input: Category): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getFavorites(): List<Category>? {
        TODO("Not yet implemented")
    }

    override suspend fun put(input: Category): Long {
        TODO("Not yet implemented")
    }

    override suspend fun put(inputs: List<Category>): List<Long>? {
        TODO("Not yet implemented")
    }

    override suspend fun get(id: String): Category? {
        TODO("Not yet implemented")
    }

    override suspend fun gets(): List<Category>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun gets(regionCode: String): List<Category>? {
        for (index in 0..keys.length) {
            try {
                val key = keys.nextKey ?: continue
                val response = service.getCategories(
                    key,
                    "id,snippet",
                    regionCode
                ).execute()
                if (response.isSuccessful) {
                    val data = response.body()?.items ?: return null
                    return mapper.gets(data)
                } else {
                    //val error = parser.parseError(response, CoinsResponse::class)
                    throw SmartError(
                        message = "error?.status?.errorMessage"
                    )
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

    override suspend fun gets(ids: List<String>): List<Category>? {
        TODO("Not yet implemented")
    }

    override suspend fun gets(offset: Long, limit: Long): List<Category>? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll() {
        TODO("Not yet implemented")
    }
}