package com.dreampany.news.api.news.remote.service

import com.dreampany.news.api.news.misc.Constants
import com.dreampany.news.api.news.remote.response.ArticlesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query

/**
 * Created by roman on 8/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface NewsApiService {
    @GET(Constants.Api.EVERYTHING)
    fun getEverything(
        @HeaderMap headers: Map<String, String>,
        @Query(Constants.Api.QUERY_IN_TITLE) queryInTitle: String,
        @Query(Constants.Api.LANGUAGE) language: String,
        @Query(Constants.Api.OFFSET) offset: Long,
        @Query(Constants.Api.LIMIT) limit: Long
    ): Call<ArticlesResponse>

    @GET(Constants.Api.HEADLINES)
    fun getHeadlinesByCountry(
        @HeaderMap headers: Map<String, String>,
        @Query(Constants.Api.COUNTRY) country: String,
        @Query(Constants.Api.OFFSET) offset: Long,
        @Query(Constants.Api.LIMIT) limit: Long
    ): Call<ArticlesResponse>

    @GET(Constants.Api.HEADLINES)
    fun getHeadlinesByCategory(
        @HeaderMap headers: Map<String, String>,
        @Query(Constants.Api.CATEGORY) category: String,
        @Query(Constants.Api.OFFSET) offset: Long,
        @Query(Constants.Api.LIMIT) limit: Long
    ): Call<ArticlesResponse>
}