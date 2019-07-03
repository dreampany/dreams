package com.dreampany.frame.api.translation.data.source

import com.dreampany.frame.api.translation.data.model.WordTranslation
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
interface YandexService {
    @Headers("Connection:close")
    @GET("api/v1.5/tr.json/translate")
    fun getTranslation(
        @Query("key") apiKey: String,
        @Query("text") text: String,
        @Query("lang") language: String,
        @Query("limit") limit: Int
    ): Call<WordTranslation>
}