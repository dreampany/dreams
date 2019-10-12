package com.dreampany.tools.injector.data

import com.dreampany.framework.misc.Remote
import com.dreampany.framework.misc.Room
import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.mapper.StationMapper
import com.dreampany.tools.data.model.Station
import com.dreampany.tools.data.source.api.StationDataSource
import com.dreampany.tools.api.radio.RadioStationService
import com.dreampany.tools.data.source.remote.RemoteStationDataSource
import com.dreampany.tools.data.source.room.RoomStationDataSource
import com.dreampany.tools.data.source.room.dao.StationDao
import com.dreampany.tools.injector.annotation.StationAnnote
import com.dreampany.tools.injector.annotation.StationItemAnnote
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.model.StationItem
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by roman on 2019-10-11
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
class RadioModule {

    @StationAnnote
    @Provides
    @Singleton
    fun provideStationSmartMap(): SmartMap<String, Station> {
        return SmartMap.newMap()
    }

    @StationAnnote
    @Provides
    @Singleton
    fun provideStationSmartCache(): SmartCache<String, Station> {
        return SmartCache.newCache()
    }

    @StationItemAnnote
    @Provides
    @Singleton
    fun provideStationItemSmartMap(): SmartMap<String, StationItem> {
        return SmartMap.newMap()
    }

    @StationItemAnnote
    @Provides
    @Singleton
    fun provideStationItemSmartCache(): SmartCache<String, StationItem> {
        return SmartCache.newCache()
    }

    @StationAnnote
    @Provides
    @Singleton
    fun provideStationRetrofit(client: OkHttpClient): Retrofit {
        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(Constants.Api.RADIO_BROWSER)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
        return retrofit;
    }

    @Provides
    @Singleton
    fun provideStationService(@StationAnnote retrofit: Retrofit): RadioStationService {
        return retrofit.create(RadioStationService::class.java);
    }


    @Room
    @Provides
    @Singleton
    fun provideRoomStationDataSource(
        mapper: StationMapper,
        dao: StationDao
    ): StationDataSource {
        return RoomStationDataSource(mapper, dao)
    }

    @Remote
    @Provides
    @Singleton
    fun provideRemoteStationDataSource(
        network: NetworkManager,
        mapper: StationMapper,
        service: RadioStationService
    ): StationDataSource {
        return RemoteStationDataSource( network, mapper, service)
    }
}