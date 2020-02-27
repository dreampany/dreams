package com.dreampany.tools.injector.data

import android.content.Context
import com.dreampany.framework.data.model.Country
import com.dreampany.framework.data.source.api.RemoteService
import com.dreampany.framework.injector.annote.Remote
import com.dreampany.framework.injector.annote.Room
import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.mapper.ServerMapper
import com.dreampany.tools.data.model.Server
import com.dreampany.tools.data.source.api.ServerDataSource
import com.dreampany.tools.data.source.remote.RemoteServerDataSource
import com.dreampany.tools.data.source.room.RoomServerDataSource
import com.dreampany.tools.data.source.room.dao.ServerDao
import com.dreampany.tools.injector.annote.CountryAnnote
import com.dreampany.tools.injector.annote.CountryItemAnnote
import com.dreampany.tools.injector.annote.ServerAnnote
import com.dreampany.tools.injector.annote.ServerItemAnnote
import com.dreampany.tools.ui.model.CountryItem
import com.dreampany.tools.ui.model.ServerItem
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by roman on 2019-10-07
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
class VpnModule {

    @Singleton
    @Provides
    @ServerAnnote
    fun provideServerSmartMap(): SmartMap<String, Server> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @ServerAnnote
    fun provideServerSmartCache(): SmartCache<String, Server> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @ServerItemAnnote
    fun provideServerItemSmartMap(): SmartMap<String, ServerItem> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @ServerItemAnnote
    fun provideServerItemSmartCache(): SmartCache<String, ServerItem> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @CountryAnnote
    fun provideCountrySmartMap(): SmartMap<String, Country> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @CountryAnnote
    fun provideCountrySmartCache(): SmartCache<String, Country> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @CountryItemAnnote
    fun provideCountryItemSmartMap(): SmartMap<String, CountryItem> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @CountryItemAnnote
    fun provideCountryItemSmartCache(): SmartCache<String, CountryItem> {
        return SmartCache.newCache()
    }

    @Room
    @Provides
    @Singleton
    fun provideRoomServerDataSource(
        mapper: ServerMapper,
        dao: ServerDao
    ): ServerDataSource {
        return RoomServerDataSource(mapper, dao)
    }

    @Remote
    @Provides
    @Singleton
    fun provideRemoteServerDataSource(
        context: Context,
        network: NetworkManager,
        mapper: ServerMapper,
        service: RemoteService
    ): ServerDataSource {
        return RemoteServerDataSource(context, network, mapper, service)
    }
}