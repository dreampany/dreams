package com.dreampany.crypto.api.inject.data

import com.dreampany.crypto.api.inject.GeckoAnnote
import com.dreampany.crypto.api.misc.Constants
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
    fun provide(gson: Gson, httpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(Constants.Apis.Gecko.BASE_URL)
            .client(httpClient)
            .build()

    @Singleton
    @Provides
    fun provideService(@GeckoAnnote retrofit: Retrofit): GeckoService =
        retrofit.create(GeckoService::class.java)
}