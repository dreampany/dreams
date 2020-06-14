package com.dreampany.tools.api.crypto.inject.data

import com.dreampany.tools.api.crypto.inject.GeckoAnnote
import com.dreampany.tools.api.crypto.remote.service.GeckoService
import com.dreampany.tools.misc.constants.CryptoConstants
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
            .baseUrl(CryptoConstants.Gecko.BASE_URL)
            .client(httpClient)
            .build()

    @Singleton
    @Provides
    fun provideGeckoService(@GeckoAnnote retrofit: Retrofit) =
        retrofit.create(GeckoService::class.java)
}