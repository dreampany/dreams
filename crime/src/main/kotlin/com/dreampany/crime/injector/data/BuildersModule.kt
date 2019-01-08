package com.dreampany.crime.injector.data

import android.app.Application
import com.dreampany.crime.injector.vm.ViewModelModule
import com.dreampany.frame.data.source.local.FlagDao
import com.dreampany.frame.data.source.local.FrameDatabase
import com.dreampany.frame.misc.AppExecutors
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */

@Module(includes = [DatabaseModule::class, SupportModule::class, ViewModelModule::class])
class BuildersModule {

    @Singleton
    @Provides
    fun provideExecutors(): AppExecutors {
        return AppExecutors()
    }
}
