package com.dreampany.translation.data.source.remote

import com.dreampany.translation.data.model.TextTranslation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * Created by roman on 2019-07-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface YandexTranslateService {
    @Headers("Connection:close")
    @GET("/api/v1.5/tr.json/translate")
    fun getTranslation(
        @Query("key") key: String,
        @Query("text") text: String,
        @Query("lang") language: String
    ): Call<TextTranslation>
}