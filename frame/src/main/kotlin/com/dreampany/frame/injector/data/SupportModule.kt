package com.dreampany.frame.injector.data

import com.dreampany.frame.data.model.Flag
import com.dreampany.frame.data.model.State
import com.dreampany.frame.data.model.Store
import com.dreampany.frame.misc.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by Hawladar Roman on 7/18/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Module
class SupportModule {

    @Singleton
    @Provides
    @FlagAnnote
    fun provideFlagSmartMap(): SmartMap<Long, Flag> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @StateAnnote
    fun provideStateSmartMap(): SmartMap<Long, State> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @StoreAnnote
    fun provideStoreSmartMap(): SmartMap<Long, Store> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @FlagAnnote
    fun provideFlagSmartCache(): SmartCache<Long, Flag> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @StateAnnote
    fun provideStateSmartCache(): SmartCache<Long, State> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @StoreAnnote
    fun provideStoreSmartCache(): SmartCache<Long, Store> {
        return SmartCache.newCache()
    }

}