package com.dreampany.tools.api.crypto.inject.data

import com.dreampany.tools.api.crypto.inject.CoinMarketCapGraphAnnote
import com.dreampany.tools.api.crypto.misc.Constants
import com.dreampany.tools.api.crypto.remote.service.CoinMarketCapGraphService
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by roman on 9/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
class CoinMarketCapGraphModule {

    @Singleton
    @Provides
    @CoinMarketCapGraphAnnote
    fun provide(gson: Gson, httpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(Constants.Apis.CoinMarketCap.BASE_GRAPH_URL)
            .client(httpClient)
            .build()

    @Singleton
    @Provides
    fun provideService(@CoinMarketCapGraphAnnote retrofit: Retrofit): CoinMarketCapGraphService =
        retrofit.create(CoinMarketCapGraphService::class.java)
}