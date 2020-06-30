package com.dreampany.tube.api.remote.service

import com.dreampany.tube.api.misc.ApiConstants
import com.dreampany.tube.api.remote.response.SearchListResponse
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
    @GET(ApiConstants.Youtube.SEARCH)
    fun getEverything(
        @Query("key") key: String,
        @Query("part") part: String,
        @Query("q") query: String
    ): Call<SearchListResponse>
}