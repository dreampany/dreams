package com.dreampany.tools.api.crypto.injector.data

import com.dreampany.tools.api.crypto.injector.annote.CryptoCompareAnnote
import com.dreampany.tools.api.crypto.misc.Constants
import com.dreampany.tools.api.crypto.remote.service.CryptoCompareService
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
class CryptoCompareModule {

    @Singleton
    @Provides
    @CryptoCompareAnnote
    fun provideCryptoCompareRetrofit(gson: Gson, httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(Constants.CryptoCompare.BASE_URL)
            .client(httpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideCryptoCompareService(@CryptoCompareAnnote retrofit: Retrofit): CryptoCompareService {
        return retrofit.create(CryptoCompareService::class.java);
    }

}