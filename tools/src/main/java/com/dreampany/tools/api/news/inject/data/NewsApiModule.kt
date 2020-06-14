package com.dreampany.tools.api.news.inject.data

import com.dreampany.crypto.api.inject.NewsApiAnnote
import com.dreampany.tools.api.news.remote.service.NewsApiService
import com.dreampany.tools.misc.constants.NewsConstants
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
class NewsApiModule {
    @Singleton
    @Provides
    @NewsApiAnnote
    fun provideNewsApiRetrofit(gson: Gson, httpClient: OkHttpClient) =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(NewsConstants.NewsApi.BASE_URL)
            .client(httpClient)
            .build()

    @Singleton
    @Provides
    fun provideNewsApiService(@NewsApiAnnote retrofit: Retrofit) =
        retrofit.create(NewsApiService::class.java)
}