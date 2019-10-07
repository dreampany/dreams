package com.dreampany.tools.injector.data

import android.content.Context
import com.dreampany.framework.data.source.api.RemoteService
import com.dreampany.framework.misc.Remote
import com.dreampany.framework.misc.Room
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.misc.ServerMapper
import com.dreampany.tools.data.source.api.ServerDataSource
import com.dreampany.tools.data.source.remote.RemoteServerDataSource
import com.dreampany.tools.data.source.room.RoomServerDataSource
import com.dreampany.tools.data.source.room.dao.ServerDao
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
class ServerModule {
    @Singleton
    @Provides
    @Remote
    fun provideRemoteServerDataSource(
        context: Context,
        network: NetworkManager,
        mapper: ServerMapper,
        service: RemoteService
    ): ServerDataSource {
        return RemoteServerDataSource(context, network, mapper, service)
    }

    @Singleton
    @Provides
    @Room
    fun provideRoomServerDataSource(
        mapper: ServerMapper,
        dao: ServerDao
    ): ServerDataSource {
        return RoomServerDataSource(mapper, dao)
    }
}