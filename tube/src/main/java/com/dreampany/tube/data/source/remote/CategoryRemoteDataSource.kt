package com.dreampany.tube.data.source.remote

import android.content.Context
import com.dreampany.framework.misc.exts.decodeBase64
import com.dreampany.framework.misc.exts.value
import com.dreampany.framework.misc.func.Keys
import com.dreampany.framework.misc.func.Parser
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tube.api.misc.ApiConstants
import com.dreampany.tube.api.remote.response.CategoryListResponse
import com.dreampany.tube.api.remote.service.YoutubeService
import com.dreampany.tube.data.model.Category
import com.dreampany.tube.data.source.api.CategoryDataSource
import com.dreampany.tube.data.source.mapper.CategoryMapper
import retrofit2.Response
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
        /*if (context.isDebug) {
            keys.setKeys(
                ApiConstants.Youtube.API_KEY_ROMAN_BJIT
            )
        } else {
            keys.setKeys(
                ApiConstants.Youtube.API_KEY_ROMAN_BJIT,
                ApiConstants.Youtube.API_KEY_DREAMPANY_PLAY_TV,
                ApiConstants.Youtube.API_KEY_DREAMPANY_MAIL
            )
        }*/
        keys.setKeys(
            ApiConstants.Youtube.API_KEY_ROMAN_BJIT.decodeBase64,
            ApiConstants.Youtube.API_KEY_DREAMPANY_PLAY_TV.decodeBase64,
            ApiConstants.Youtube.API_KEY_DREAMPANY_MAIL.decodeBase64
        )
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
        for (index in 0..keys.indexLength) {
            try {
                val key = keys.nextKey ?: continue
                val response: Response<CategoryListResponse> = service.getCategories(
                    key,
                    "id,snippet",
                    regionCode
                ).execute()

                if (response.isSuccessful) {
                    val data = response.body()?.items ?: return null
                    return mapper.gets(data)
                } else {
                    val error = parser.parseError(response, CategoryListResponse::class)
                    throw SmartError(
                        message = error?.error?.message,
                        code = error?.error?.code.value
                    )
                }
            } catch (error: Throwable) {
                if (error is SmartError) {
                    if (error.code != 403)
                        throw error
                }
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