package com.dreampany.translation.injector

import com.dreampany.firebase.RxFirebaseFirestore
import com.dreampany.firebase.RxFirebaseTranslation
import com.dreampany.frame.api.key.KeyManager
import com.dreampany.frame.misc.Firestore
import com.dreampany.frame.misc.Machine
import com.dreampany.frame.misc.Remote
import com.dreampany.frame.misc.Room
import com.dreampany.network.manager.NetworkManager
import com.dreampany.translation.data.misc.TextTranslationMapper
import com.dreampany.translation.data.source.api.TranslationDataSource
import com.dreampany.translation.data.source.firestore.FirestoreTranslationDataSource
import com.dreampany.translation.data.source.machine.MachineTranslationDataSource
import com.dreampany.translation.data.source.remote.RemoteTranslationDataSource
import com.dreampany.translation.data.source.remote.YandexTranslationService
import com.dreampany.translation.data.source.room.RoomTranslationDataSource
import com.dreampany.translation.data.source.room.TextTranslationDao
import com.dreampany.translation.misc.Constants
import com.dreampany.translation.misc.YandexTranslation
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
class TranslationModule {

    @Provides
    @Singleton
    @YandexTranslation
    fun provideYandexTranslationRetrofit(gson: Gson, httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(Constants.Yandex.TRANSLATE_BASE_URL)
            .client(httpClient)
            .build()
    }

    @Provides
    fun provideYandexTranslationService(@YandexTranslation retrofit: Retrofit): YandexTranslationService {
        return retrofit.create(YandexTranslationService::class.java);
    }

    @Singleton
    @Provides
    @Room
    fun provideRoomTranslationDataSource(
        network: NetworkManager,
        mapper: TextTranslationMapper,
        dao: TextTranslationDao
    ): TranslationDataSource {
        return RoomTranslationDataSource(mapper, dao)
    }

    @Singleton
    @Provides
    @Machine
    fun provideMachineTranslationDataSource(
        network: NetworkManager,
        mapper: TextTranslationMapper,
        tranlation: RxFirebaseTranslation
    ): TranslationDataSource {
        return MachineTranslationDataSource(network, mapper, tranlation)
    }

    @Singleton
    @Provides
    @Firestore
    fun provideFirestoreTranslationDataSource(
        network: NetworkManager,
        mapper: TextTranslationMapper,
        firestore: RxFirebaseFirestore
    ): TranslationDataSource {
        return FirestoreTranslationDataSource(network, mapper, firestore)
    }


    @Singleton
    @Provides
    @Remote
    fun provideRemoteTranslationDataSource(
        network: NetworkManager,
        mapper: TextTranslationMapper,
        key: KeyManager,
        service: YandexTranslationService
    ): TranslationDataSource {
        return RemoteTranslationDataSource(network, mapper, key, service)
    }
}