package com.dreampany.radio.injector.data

import com.dreampany.radio.data.model.Demo
import com.dreampany.radio.misc.StationAnnote
import com.dreampany.frame.misc.SmartCache
import com.dreampany.frame.misc.SmartMap
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by Hawladar Roman on 7/11/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Module
class SupportModule {

    @Singleton
    @Provides
    @StationAnnote
    fun provideStationSmartMap(): SmartMap<Long, Demo> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @StationAnnote
    fun provideStationSmartCache(): SmartCache<Long, Demo> {
        return SmartCache.newCache()
    }
}