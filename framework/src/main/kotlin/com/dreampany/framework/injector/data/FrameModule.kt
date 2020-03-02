package com.dreampany.framework.injector.data

import com.dreampany.framework.data.misc.PointMapper
import com.dreampany.framework.data.misc.StoreMapper
import com.dreampany.framework.data.source.api.PointDataSource
import com.dreampany.framework.data.source.api.StoreDataSource
import com.dreampany.framework.data.source.room.RoomPointDataSource
import com.dreampany.framework.data.source.room.RoomStoreDataSource
import com.dreampany.framework.data.source.room.dao.PointDao
import com.dreampany.framework.data.source.room.dao.StoreDao
import com.dreampany.framework.injector.annote.Room
import com.dreampany.framework.injector.http.HttpModule
import com.dreampany.framework.injector.json.JsonModule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by roman on 2/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module(includes = [
    SupportModule::class,
    DatabaseModule::class,
    JsonModule::class,
    HttpModule::class,
    FirebaseModule::class])
class FrameModule {

    @Singleton
    @Provides
    @Room
    fun provideRoomStoreDataSource(
        mapper: StoreMapper,
        dao: StoreDao
    ): StoreDataSource {
        return RoomStoreDataSource(mapper, dao)
    }

    @Singleton
    @Provides
    @Room
    fun provideRoomPointDataSource(
        mapper: PointMapper,
        dao: PointDao
    ): PointDataSource {
        return RoomPointDataSource(mapper, dao)
    }
}