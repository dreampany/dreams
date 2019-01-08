package com.dreampany.media.injector.data

import com.dreampany.frame.misc.Memory
import com.dreampany.frame.misc.Room
import com.dreampany.media.data.misc.ApkMapper
import com.dreampany.media.data.misc.ImageMapper
import com.dreampany.media.data.model.Apk
import com.dreampany.media.data.model.Image
import com.dreampany.media.data.source.api.MediaDataSource
import com.dreampany.media.data.source.room.ApkDao
import com.dreampany.media.data.source.room.ApkRoomDataSource
import com.dreampany.media.data.source.room.ImageDao
import com.dreampany.media.data.source.room.ImageRoomDataSource
import com.dreampany.media.data.source.memory.ApkMemoryDataSource
import com.dreampany.media.data.source.memory.ApkProvider
import com.dreampany.media.data.source.memory.ImageMemoryDataSource
import com.dreampany.media.data.source.memory.ImageProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by Hawladar Roman on 7/16/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

@Module(includes = [SupportModule::class, DatabaseModule::class])
class MediaModule {

    @Singleton
    @Provides
    @Memory
    fun provideApkMemoryDataSource(provider: ApkProvider): MediaDataSource<Apk> {
        return ApkMemoryDataSource(provider)
    }

    @Singleton
    @Provides
    @Memory
    fun provideImageMemoryDataSource(provider: ImageProvider): MediaDataSource<Image> {
        return ImageMemoryDataSource(provider)
    }

    @Singleton
    @Provides
    @Room
    fun provideApkRoomDataSource(mapper: ApkMapper,
                                  dao: ApkDao): MediaDataSource<Apk> {
        return ApkRoomDataSource(mapper, dao)
    }

    @Singleton
    @Provides
    @Room
    fun provideImageRoomDataSource(mapper: ImageMapper,
                                    dao: ImageDao): MediaDataSource<Image> {
        return ImageRoomDataSource(mapper, dao)
    }
}