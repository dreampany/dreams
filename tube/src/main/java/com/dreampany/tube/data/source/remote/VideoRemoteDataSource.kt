package com.dreampany.tube.data.source.remote

import android.content.Context
import com.dreampany.framework.misc.constant.Constants
import com.dreampany.framework.misc.exts.decodeBase64
import com.dreampany.framework.misc.exts.value
import com.dreampany.framework.misc.func.Keys
import com.dreampany.framework.misc.func.Parser
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tube.api.misc.ApiConstants
import com.dreampany.tube.api.remote.response.SearchListResponse
import com.dreampany.tube.api.remote.response.VideoListResponse
import com.dreampany.tube.api.remote.service.YoutubeService
import com.dreampany.tube.data.model.Video
import com.dreampany.tube.data.source.api.VideoDataSource
import com.dreampany.tube.data.source.mapper.VideoMapper
import retrofit2.Response
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

    override suspend fun putOfRegionCode(regionCode: String, inputs: List<Video>) {
        TODO("Not yet implemented")
    }

    override suspend fun putOfEvent(eventType: String, inputs: List<Video>) {
        TODO("Not yet implemented")
    }

    override suspend fun putIf(inputs: List<Video>): List<Long>? {
        TODO("Not yet implemented")
    }

    override suspend fun isExists(id: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun get(id: String): Video? {
        TODO("Not yet implemented")
    }

    override suspend fun gets(): List<Video>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun gets(ids: List<String>): List<Video>? {
        for (index in 0..keys.indexLength) {
            try {
                val key = keys.nextKey ?: continue
                val part = "snippet,contentDetails,statistics"
                val id = ids.joinToString(Constants.Sep.COMMA.toString())
                val response : Response<VideoListResponse> = service.getVideosOfId(
                    key,
                    part,
                    id
                ).execute()
                if (response.isSuccessful) {
                    val data = response.body()?.items ?: return null
                    return mapper.gets(data)
                } else {
                    val error = parser.parseError(response, VideoListResponse::class)
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
        for (index in 0..keys.indexLength) {
            try {
                val key = keys.nextKey ?: continue
                val part = "snippet"
                val type = "video"
                val order = "viewCount"
                val response : Response<SearchListResponse> = service.getSearchResultOfCategoryId(
                    key,
                    part,
                    type,
                    categoryId,
                    order,
                    limit
                ).execute()
                if (response.isSuccessful) {
                    val data = response.body()?.items ?: return null
                    return mapper.getsOfSearch(categoryId, data)
                } else {
                    val error = parser.parseError(response, SearchListResponse::class)
                    throw SmartError(
                        message = error?.error?.message,
                        code = error?.error?.code.value
                    )
                }
            } catch (error: Throwable) {
                if (error is SmartError) {
                    if (!error.isForbidden)
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

    @Throws
    override suspend fun getsOfRegionCode(
        regionCode: String,
        offset: Long,
        limit: Long
    ): List<Video>? {
        for (index in 0..keys.indexLength) {
            try {
                val key = keys.nextKey ?: continue
                val part = "snippet"
                val type = "video"
                val order = "viewCount"
                val response : Response<SearchListResponse> = service.getSearchResultOfRegionCode(
                    key,
                    part,
                    type,
                    regionCode,
                    order,
                    limit
                ).execute()
                if (response.isSuccessful) {
                    val data = response.body()?.items ?: return null
                    return mapper.getsOfSearch(regionCode, data)
                } else {
                    val error = parser.parseError(response, SearchListResponse::class)
                    throw SmartError(
                        message = error?.error?.message,
                        code = error?.error?.code.value
                    )
                }
            } catch (error: Throwable) {
                if (error is SmartError) {
                    if (!error.isForbidden)
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

    @Throws
    override suspend fun getsOfEvent(eventType : String, offset: Long, limit: Long): List<Video>? {
        for (index in 0..keys.indexLength) {
            try {
                val key = keys.nextKey ?: continue
                val part = "snippet"
                val type = "video"
                val order = "viewCount"
                val response : Response<SearchListResponse> = service.getSearchResultOfEvent(
                    key,
                    part,
                    type,
                    eventType,
                    order,
                    limit
                ).execute()
                if (response.isSuccessful) {
                    val data = response.body()?.items ?: return null
                    return mapper.getsOfSearch(eventType, data)
                } else {
                    val error = parser.parseError(response, SearchListResponse::class)
                    throw SmartError(
                        message = error?.error?.message,
                        code = error?.error?.code.value
                    )
                }
            } catch (error: Throwable) {
                if (error is SmartError) {
                    if (!error.isForbidden)
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

}