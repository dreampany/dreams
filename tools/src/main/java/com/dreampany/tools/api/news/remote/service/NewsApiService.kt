package com.dreampany.tools.api.news.remote.service

import com.dreampany.tools.api.news.misc.Constants
import com.dreampany.tools.api.news.remote.response.ArticlesResponse
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
    @GET(Constants.Apis.NewsApi.EVERYTHING)
    fun getEverything(
        @HeaderMap headers: Map<String, String>,
        @Query(Constants.Apis.NewsApi.QUERY_IN_TITLE) queryInTitle: String,
        @Query(Constants.Apis.NewsApi.LANGUAGE) language: String,
        @Query(Constants.Apis.NewsApi.OFFSET) offset: Long,
        @Query(Constants.Apis.NewsApi.LIMIT) limit: Long
    ): Call<ArticlesResponse>

    @GET(Constants.Apis.NewsApi.HEADLINES)
    fun getHeadlinesByCountry(
        @HeaderMap headers: Map<String, String>,
        @Query(Constants.Apis.NewsApi.COUNTRY) country: String,
        @Query(Constants.Apis.NewsApi.OFFSET) offset: Long,
        @Query(Constants.Apis.NewsApi.LIMIT) limit: Long
    ): Call<ArticlesResponse>

    @GET(Constants.Apis.NewsApi.HEADLINES)
    fun getHeadlinesByCategory(
        @HeaderMap headers: Map<String, String>,
        @Query(Constants.Apis.NewsApi.CATEGORY) category: String,
        @Query(Constants.Apis.NewsApi.OFFSET) offset: Long,
        @Query(Constants.Apis.NewsApi.LIMIT) limit: Long
    ): Call<ArticlesResponse>
}