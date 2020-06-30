package com.dreampany.tube.api.inject.data

import com.dreampany.tube.api.inject.annote.YoutubeAnnote
import com.dreampany.tube.api.misc.ApiConstants
import com.dreampany.tube.api.remote.service.YoutubeService
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by roman on 8/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
class YoutubeModule {

    @Singleton
    @Provides
    @YoutubeAnnote
    fun provideYoutubeApiRetrofit(gson: Gson, httpClient: OkHttpClient) =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(ApiConstants.Youtube.BASE_URL)
            .client(httpClient)
            .build()

    @Singleton
    @Provides
    fun provideYoutubeApiService(@YoutubeAnnote retrofit: Retrofit) =
        retrofit.create(YoutubeService::class.java)
}