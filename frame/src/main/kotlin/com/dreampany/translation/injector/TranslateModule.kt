package com.dreampany.translation.injector

import com.dreampany.frame.misc.Firestore
import com.dreampany.frame.misc.Remote
import com.dreampany.frame.misc.Room
import com.dreampany.network.manager.NetworkManager
import com.dreampany.translation.data.misc.TextTranslateMapper
import com.dreampany.translation.data.source.api.TextTranslationDataSource
import com.dreampany.translation.data.source.firestore.TextTranslationFirestoreDataSource
import com.dreampany.translation.data.source.remote.TextTranslationRemoteDataSource
import com.dreampany.translation.data.source.remote.YandexTranslateService
import com.dreampany.translation.data.source.room.TextTranslationDao
import com.dreampany.translation.data.source.room.TextTranslationRoomDataSource
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
    fun provideTextTranslateRoomDataSource(
        network: NetworkManager,
        mapper: TextTranslateMapper,
        dao: TextTranslationDao
    ): TextTranslationDataSource {
        return TextTranslationRoomDataSource(mapper, dao)
    }

    @Singleton
    @Provides
    @Firestore
    fun provideTextTranslateFirestoreDataSource(
        network: NetworkManager,
        mapper: TextTranslateMapper
    ): TextTranslationDataSource {
        return TextTranslationFirestoreDataSource(network, mapper)
    }


    @Singleton
    @Provides
    @Remote
    fun provideTextTranslateRemoteDataSource(
        network: NetworkManager,
        mapper: TextTranslateMapper,
        service: YandexTranslateService
    ): TextTranslationDataSource {
        return TextTranslationRemoteDataSource(network, mapper, service)
    }
}