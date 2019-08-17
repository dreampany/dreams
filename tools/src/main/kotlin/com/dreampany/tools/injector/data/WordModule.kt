package com.dreampany.tools.injector.data

import android.content.Context
import com.dreampany.frame.misc.*
import com.dreampany.tools.data.misc.WordMapper
import com.dreampany.tools.data.source.api.WordDataSource
import com.dreampany.tools.data.source.assets.AssetsWordDataSource
import com.dreampany.tools.data.source.dao.WordDao
import com.dreampany.tools.data.source.firestore.FirestoreWordDataSource
import com.dreampany.tools.data.source.remote.RemoteWordDataSource
import com.dreampany.tools.data.source.room.RoomWordDataSource
import com.dreampany.tools.data.source.vision.VisionWordDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
class WordModule {

    @Singleton
    @Provides
    @Assets
    fun provideAssetsWordDataSource(
        context: Context,
        mapper: WordMapper
    ): WordDataSource {
        return AssetsWordDataSource(context, mapper)
    }

    @Singleton
    @Provides
    @Room
    fun provideRoomWordDataSource(
        mapper: WordMapper,
        dao: WordDao
    ): WordDataSource {
        return RoomWordDataSource(mapper, dao)
    }

    @Singleton
    @Provides
    @Firestore
    fun provideFirestoreWordDataSource(
        mapper: WordMapper,
        dao: WordDao
    ): WordDataSource {
        return FirestoreWordDataSource(mapper, dao)
    }

    @Singleton
    @Provides
    @Remote
    fun provideRemoteWordDataSource(
        mapper: WordMapper,
        dao: WordDao
    ): WordDataSource {
        return RemoteWordDataSource(mapper, dao)
    }

    @Singleton
    @Provides
    @Vision
    fun provideVisionWordDataSource(
        mapper: WordMapper,
        dao: WordDao
    ): WordDataSource {
        return VisionWordDataSource(mapper, dao)
    }
}