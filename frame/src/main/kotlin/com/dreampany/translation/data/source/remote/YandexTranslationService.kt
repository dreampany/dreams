package com.dreampany.translation.data.source.remote

import com.dreampany.translation.data.model.TextTranslationResponse
import com.dreampany.translation.misc.Constants
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Created by roman on 2019-07-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface YandexTranslationService {
    @FormUrlEncoded
    @Headers(Constants.Network.HEADER_CONNECTION_CLOSE)
    @POST(Constants.Yandex.TRANSLATION_END_POINT)
    fun getTranslation(
        @Field(Constants.Yandex.KEY) key: String,
        @Field(Constants.Yandex.TEXT) text: String,
        @Field(Constants.Yandex.LANGUAGE) language: String
    ): Call<TextTranslationResponse>
}