package com.dreampany.media.injector.data

import android.app.Application
import com.dreampany.media.data.source.room.ApkDao
import com.dreampany.media.data.source.room.ImageDao
import com.dreampany.media.data.source.room.MediaDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by Hawladar Roman on 7/21/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideMediaDatabase(application: Application): MediaDatabase {
        return MediaDatabase.onInstance(application.applicationContext)
    }

    @Singleton
    @Provides
    fun provideApkDao(database: MediaDatabase): ApkDao {
        return database.apkDao()
    }

    @Singleton
    @Provides
    fun provideImageDao(database: MediaDatabase): ImageDao {
        return database.imageDao()
    }
}