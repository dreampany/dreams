package com.dreampany.radio.injector.data

import com.dreampany.frame.data.source.repository.FlagRepository
import com.dreampany.radio.injector.vm.ViewModelModule
import com.dreampany.frame.injector.data.FrameModule
import com.dreampany.frame.misc.Room
import com.dreampany.radio.data.misc.StationMapper
import com.dreampany.radio.data.source.api.StationDataSource
import com.dreampany.radio.data.source.room.StationDao
import com.dreampany.radio.data.source.room.StationRoomDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */

@Module(includes = [FrameModule::class, DatabaseModule::class, SupportModule::class, ViewModelModule::class])
class BuildersModule {

    @Singleton
    @Provides
    @Room
    fun provideRoomStationDataSource(mapper: StationMapper,
                                  dao: StationDao): StationDataSource {
        return StationRoomDataSource(mapper, dao)
    }
}
