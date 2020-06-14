package com.dreampany.tools.api.news.remote.service

import com.dreampany.tools.api.news.remote.response.ArticlesResponse
import com.dreampany.tools.misc.constants.NewsConstants
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
    @GET(NewsConstants.NewsApi.EVERYTHING)
    fun getEverything(
        @HeaderMap headers: Map<String, String>,
        @Query(NewsConstants.NewsApi.QUERY_IN_TITLE) queryInTitle: String,
        @Query(NewsConstants.NewsApi.LANGUAGE) language: String,
        @Query(NewsConstants.NewsApi.OFFSET) offset: Long,
        @Query(NewsConstants.NewsApi.LIMIT) limit: Long
    ): Call<ArticlesResponse>
}