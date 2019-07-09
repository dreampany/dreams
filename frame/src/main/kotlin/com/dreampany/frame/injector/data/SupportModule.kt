package com.dreampany.frame.injector.data

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
    @StateAnnote
    fun provideStateSmartMap(): SmartMap<String, State> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @StoreAnnote
    fun provideStoreSmartMap(): SmartMap<String, Store> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @StateAnnote
    fun provideStateSmartCache(): SmartCache<String, State> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @StoreAnnote
    fun provideStoreSmartCache(): SmartCache<String, Store> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @Favorite
    fun provideFavoriteSmartMap(): SmartMap<String, Boolean> {
        return SmartMap.newMap()
    }
}