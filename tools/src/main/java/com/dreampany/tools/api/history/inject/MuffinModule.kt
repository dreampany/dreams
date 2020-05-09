package com.dreampany.tools.api.history.inject

import com.dreampany.tools.api.history.Constants
import com.dreampany.tools.api.history.MuffinService
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by roman on 9/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
class MuffinModule {

    @Singleton
    @Provides
    @MuffinAnnote
    fun provideMuffinRetrofit(gson: Gson, httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(Constants.Api.HISTORY_MUFFIN_LABS)
            .client(httpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideMuffinService(@MuffinAnnote retrofit: Retrofit): MuffinService {
        return retrofit.create(MuffinService::class.java);
    }
}