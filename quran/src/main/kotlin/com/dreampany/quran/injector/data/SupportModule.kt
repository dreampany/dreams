package com.dreampany.quran.injector.data

import com.dreampany.quran.data.model.Demo
import com.dreampany.quran.misc.DemoAnnote
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
    @DemoAnnote
    fun provideDemoSmartMap(): SmartMap<Long, Demo> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @DemoAnnote
    fun provideDemoSmartCache(): SmartCache<Long, Demo> {
        return SmartCache.newCache()
    }
}