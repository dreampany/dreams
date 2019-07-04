package com.dreampany.translation.injector

import com.dreampany.frame.misc.Remote
import com.dreampany.frame.misc.Room
import com.dreampany.network.manager.NetworkManager
import com.dreampany.translation.data.misc.TextTranslateMapper
import com.dreampany.translation.data.source.api.TextTranslateDataSource
import com.dreampany.translation.data.source.remote.TextTranslateRemoteDataSource
import com.dreampany.translation.data.source.remote.YandexTranslateService
import com.dreampany.translation.data.source.room.TextTranslateDao
import com.dreampany.translation.data.source.room.TextTranslateRoomDataSource
import com.dreampany.translation.misc.Constants
import com.dreampany.translation.misc.YandexTranslate
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


/**
 * Created by roman on 2019-07-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module(includes = [SupportModule::class, DatabaseModule::class])
class TranslateModule {

    @Provides
    @Singleton
    @YandexTranslate
    fun provideYandexTranslateRetrofit(gson: Gson, httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(Constants.Yandex.TRANSLATE_BASE_URL)
            .client(httpClient)
            .build()
    }

    @Provides
    fun provideYandexTranslateService(@YandexTranslate retrofit: Retrofit): YandexTranslateService {
        return retrofit.create(YandexTranslateService::class.java);
    }

    @Singleton
    @Provides
    @Room
    fun provideTextRoomDataSource(
        network: NetworkManager,
        mapper: TextTranslateMapper,
        dao: TextTranslateDao
    ): TextTranslateDataSource {
        return TextTranslateRoomDataSource(mapper, dao)
    }

    @Singleton
    @Provides
    @Remote
    fun provideTextRemoteDataSource(
        network: NetworkManager,
        mapper: TextTranslateMapper,
        service: YandexTranslateService
    ): TextTranslateDataSource {
        return TextTranslateRemoteDataSource(network, mapper, service)
    }
}