package com.dreampany.tube.api.remote.service

import com.dreampany.tube.api.misc.ApiConstants
import com.dreampany.tube.api.remote.response.SearchListResponse
import com.dreampany.tube.api.remote.response.CategoryListResponse
import com.dreampany.tube.api.remote.response.VideoListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by roman on 8/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface YoutubeService {

    @GET(ApiConstants.Youtube.VIDEO_CATEGORIES)
    fun getCategories(
        @Query("key") key: String,
        @Query("part") part: String,
        @Query("regionCode") regionCode: String
    ): Call<CategoryListResponse>

    @GET(ApiConstants.Youtube.SEARCH)
    fun getSearchResultOfQuery(
        @Query("key") key: String,
        @Query("part") part: String,
        @Query("q") query: String
    ): Call<SearchListResponse>

    @GET(ApiConstants.Youtube.SEARCH)
    fun getSearchResultOfCategoryId(
        @Query("key") key: String,
        @Query("part") part: String,
        @Query("type") type: String,
        @Query("videoCategoryId") categoryId: String,
        @Query("order") order: String,
        @Query("maxResults") limit: Long
    ): Call<SearchListResponse>

    @GET(ApiConstants.Youtube.SEARCH)
    fun getSearchResultOfRegionCode(
        @Query("key") key: String,
        @Query("part") part: String,
        @Query("type") type: String,
        @Query("regionCode") regionCode: String,
        @Query("order") order: String,
        @Query("maxResults") limit: Long
    ): Call<SearchListResponse>

    @GET(ApiConstants.Youtube.SEARCH)
    fun getSearchResultOfEvent(
        @Query("key") key: String,
        @Query("part") part: String,
        @Query("type") type: String,
        @Query("eventType") eventType: String,
        @Query("order") order: String,
        @Query("maxResults") limit: Long
    ): Call<SearchListResponse>

    @GET(ApiConstants.Youtube.VIDEOS)
    fun getVideosOfChartCategoryId(
        @Query("key") key: String,
        @Query("part") part: String,
        @Query("chart") chart: String,
        @Query("videoCategoryId") categoryId: String
    ): Call<VideoListResponse>

    @GET(ApiConstants.Youtube.VIDEOS)
    fun getVideosOfId(
        @Query("key") key: String,
        @Query("part") part: String,
        @Query("id") id: String
    ): Call<VideoListResponse>
}