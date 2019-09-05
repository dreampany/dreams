package com.dreampany.framework.injector.data

import com.dreampany.framework.data.misc.StoreMapper
import com.dreampany.framework.data.source.api.StoreDataSource
import com.dreampany.framework.data.source.room.dao.StoreDao
import com.dreampany.framework.data.source.room.RoomStoreDataSource
import com.dreampany.framework.injector.network.HttpModule
import com.dreampany.framework.misc.Room
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

/*    @Singleton
    @Provides
    @Room
    fun provideRoomStateDataSource(
        mapper: StateMapper,
        dao: StateDao
    ): StateDataSource {
        return RoomStateDataSource(mapper, dao)
    }*/

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