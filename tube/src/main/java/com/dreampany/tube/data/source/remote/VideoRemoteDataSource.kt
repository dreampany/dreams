package com.dreampany.tube.data.source.remote

import android.content.Context
import com.dreampany.framework.misc.exts.isDebug
import com.dreampany.framework.misc.func.Keys
import com.dreampany.framework.misc.func.Parser
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tube.api.misc.ApiConstants
import com.dreampany.tube.api.remote.service.YoutubeService
import com.dreampany.tube.data.model.Video
import com.dreampany.tube.data.source.api.VideoDataSource
import com.dreampany.tube.data.source.mapper.VideoMapper
import java.net.UnknownHostException

/**
 * Created by roman on 30/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class VideoRemoteDataSource(
    private val context: Context,
    private val network: NetworkManager,
    private val parser: Parser,
    private val keys: Keys,
    private val mapper: VideoMapper,
    private val service: YoutubeService
) : VideoDataSource {

    init {
        keys.setKeys(ApiConstants.Youtube.API_KEY_ROMAN_BJIT)
        if (!context.isDebug) {
            keys.setKeys(ApiConstants.Youtube.API_KEY_DREAMPANY_MAIL)
        }
    }

    override suspend fun isFavorite(input: Video): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFavorite(input: Video): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getFavorites(): List<Video>? {
        TODO("Not yet implemented")
    }

    override suspend fun put(input: Video): Long {
        TODO("Not yet implemented")
    }

    override suspend fun put(inputs: List<Video>): List<Long>? {
        TODO("Not yet implemented")
    }

    override suspend fun get(id: String): Video? {
        TODO("Not yet implemented")
    }

    override suspend fun gets(): List<Video>? {
        TODO("Not yet implemented")
    }

    override suspend fun gets(ids: List<String>): List<Video>? {
        TODO("Not yet implemented")
    }

    override suspend fun gets(offset: Long, limit: Long): List<Video>? {
        TODO("Not yet implemented")
    }

    override suspend fun getsOfQuery(query: String): List<Video>? {
        TODO("Not yet implemented")
    }

    override suspend fun getsOfCategoryId(categoryId: String): List<Video>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun getsOfCategoryId(
        categoryId: String,
        offset: Long,
        limit: Long
    ): List<Video>? {
        for (index in 0..keys.length) {
            try {
                val key = keys.nextKey ?: continue
                val part = "snippet"
                val type = "video"
                val order = "viewCount"
                val response = service.getSearchResultOfCategoryId(
                    key,
                    part,
                    type,
                    order,
                    categoryId
                ).execute()
                if (response.isSuccessful) {
                    val data = response.body()?.items ?: return null
                    return mapper.getsOfSearch(categoryId, data)
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

}