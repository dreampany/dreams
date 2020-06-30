package com.dreampany.crypto.api.inject.data

import com.dreampany.crypto.api.inject.annote.GeckoAnnote
import com.dreampany.crypto.api.misc.ApiConstants
import com.dreampany.crypto.api.remote.service.GeckoService
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by roman on 11/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
class GeckoModule {
    @Singleton
    @Provides
    @GeckoAnnote
    fun provideGeckoRetrofit(gson: Gson, httpClient: OkHttpClient) =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(ApiConstants.Gecko.BASE_URL)
            .client(httpClient)
            .build()

    @Singleton
    @Provides
    fun provideGeckoService(@GeckoAnnote retrofit: Retrofit) =
        retrofit.create(GeckoService::class.java)
}