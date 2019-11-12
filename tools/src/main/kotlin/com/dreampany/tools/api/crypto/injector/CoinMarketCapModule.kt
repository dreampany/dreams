package com.dreampany.tools.api.crypto.injector

import com.dreampany.tools.api.crypto.misc.CoinMarketCapAnnote
import com.dreampany.tools.api.crypto.misc.Constants
import com.dreampany.tools.api.crypto.remote.CoinMarketCapService
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
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
    fun provideCoinMarketCapRetrofit(gson: Gson, httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(Constants.CoinMarketCap.BASE_URL)
            .client(httpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideCoinMarketCapService(@CoinMarketCapAnnote retrofit: Retrofit): CoinMarketCapService {
        return retrofit.create(CoinMarketCapService::class.java);
    }

}