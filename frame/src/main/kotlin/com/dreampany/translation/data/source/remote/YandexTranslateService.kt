package com.dreampany.translation.data.source.remote

import com.dreampany.translation.data.model.TextTranslationResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Created by roman on 2019-07-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface YandexTranslateService {
    @Headers("Connection:close")
    @POST("/api/v1.5/tr.json/translate")
    fun getTranslation(
        @Field("key") key: String,
        @Field("text") text: String,
        @Field("lang") language: String
    ): Call<TextTranslationResponse>
}