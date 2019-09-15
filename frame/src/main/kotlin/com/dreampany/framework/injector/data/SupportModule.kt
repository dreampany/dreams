package com.dreampany.framework.injector.data

import com.dreampany.framework.data.model.Point
import com.dreampany.framework.data.model.Store
import com.dreampany.framework.misc.*
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
    @Favorite
    fun provideFavoriteSmartMap(): SmartMap<String, Boolean> {
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
    @StoreAnnote
    fun provideStoreSmartCache(): SmartCache<String, Store> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @PointAnnote
    fun providePointSmartMap(): SmartMap<String, Point> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @StoreAnnote
    fun providePointSmartCache(): SmartCache<String, Point> {
        return SmartCache.newCache()
    }
}