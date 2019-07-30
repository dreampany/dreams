package com.dreampany.history.injector.data

import com.dreampany.frame.misc.SmartCache
import com.dreampany.frame.misc.SmartMap
import com.dreampany.history.data.model.History
import com.dreampany.history.misc.HistoryAnnote
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
    @HistoryAnnote
    fun provideHistorySmartMap(): SmartMap<String, History> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @HistoryAnnote
    fun provideHistorySmartCache(): SmartCache<String, History> {
        return SmartCache.newCache()
    }
}