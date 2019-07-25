package com.dreampany.frame.injector.data

import com.dreampany.frame.data.misc.StateMapper
import com.dreampany.frame.data.misc.StoreMapper
import com.dreampany.frame.data.source.api.StateDataSource
import com.dreampany.frame.data.source.api.StoreDataSource
import com.dreampany.frame.data.source.dao.StateDao
import com.dreampany.frame.data.source.dao.StoreDao
import com.dreampany.frame.data.source.room.RoomStateDataSource
import com.dreampany.frame.data.source.room.RoomStoreDataSource
import com.dreampany.frame.injector.network.HttpModule
import com.dreampany.frame.misc.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by Hawladar Roman on 7/16/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

@Module(includes = [SupportModule::class, DatabaseModule::class, HttpModule::class, FirebaseModule::class])
class FrameModule {

    @Singleton
    @Provides
    @Room
    fun provideRoomStateDataSource(
        mapper: StateMapper,
        dao: StateDao
    ): StateDataSource {
        return RoomStateDataSource(mapper, dao)
    }

    @Singleton
    @Provides
    @Room
    fun provideRoomStoreDataSource(
        mapper: StoreMapper,
        dao: StoreDao
    ): StoreDataSource {
        return RoomStoreDataSource(mapper, dao)
    }
}