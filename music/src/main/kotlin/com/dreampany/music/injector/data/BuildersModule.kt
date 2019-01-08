package com.dreampany.music.injector.data

import android.app.Application
import com.dreampany.frame.data.source.local.FlagDao
import com.dreampany.frame.data.source.local.FrameDatabase
import com.dreampany.frame.misc.AppExecutors
import com.dreampany.music.injector.vm.ViewModelModule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */

@Module(includes = [ViewModelModule::class])
class BuildersModule {

    @Singleton
    @Provides
    fun provideFrameDatabase(application: Application): FrameDatabase {
        return FrameDatabase.onInstance(application.applicationContext)
    }

    @Singleton
    @Provides
    fun provideFlagDao(database: FrameDatabase): FlagDao {
        return database.flagDao()
    }

    @Singleton
    @Provides
    fun provideExecutors(): AppExecutors {
        return AppExecutors()
    }
}
