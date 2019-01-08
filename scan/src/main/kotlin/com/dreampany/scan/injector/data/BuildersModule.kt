package com.dreampany.scan.injector.data

import com.dreampany.frame.data.source.local.FlagDao
import com.dreampany.frame.misc.AppExecutors
import com.dreampany.frame.misc.Local
import com.dreampany.scan.data.source.ScanDataSource
import com.dreampany.scan.data.source.local.LocalScanDataSource
import com.dreampany.scan.data.source.local.ScanDao
import com.dreampany.scan.injector.vm.ViewModelModule
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
    @Local
    fun provideLocalScanDataSource(flagDao: FlagDao, scanDao: ScanDao): ScanDataSource {
        return LocalScanDataSource(flagDao, scanDao)
    }

    @Singleton
    @Provides
    fun provideExecutors(): AppExecutors {
        return AppExecutors()
    }
}
