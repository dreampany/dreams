package com.dreampany.crypto.api.remote.service

import com.dreampany.crypto.api.misc.ApiConstants
import com.dreampany.crypto.api.remote.response.ArticlesResponse
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
    @GET(ApiConstants.NewsApi.EVERYTHING)
    fun getEverything(
        @HeaderMap headers: Map<String, String>,
        @Query(ApiConstants.NewsApi.QUERY_IN_TITLE) queryInTitle: String,
        @Query(ApiConstants.NewsApi.LANGUAGE) language: String,
        @Query(ApiConstants.NewsApi.OFFSET) offset: Long,
        @Query(ApiConstants.NewsApi.LIMIT) limit: Long
    ): Call<ArticlesResponse>
}