package com.dreampany.crypto.api.inject.data

import com.dreampany.crypto.api.inject.annote.CoinMarketCapAnnote
import com.dreampany.crypto.api.misc.ApiConstants
import com.dreampany.crypto.api.remote.service.CoinMarketCapService
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by roman on 2019-11-12
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
class CoinMarketCapModule {

    @Singleton
    @Provides
    @CoinMarketCapAnnote
    fun provideCoinMarketCapRetrofit(gson: Gson, httpClient: OkHttpClient) =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(ApiConstants.CoinMarketCap.BASE_URL)
            .client(httpClient)
            .build()

    @Singleton
    @Provides
    fun provideCoinMarketCapService(@CoinMarketCapAnnote retrofit: Retrofit) =
        retrofit.create(CoinMarketCapService::class.java)
}