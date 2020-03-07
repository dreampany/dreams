package com.dreampany.framework.injector.data

import com.dreampany.framework.data.model.Point
import com.dreampany.framework.data.model.Store
import com.dreampany.framework.injector.annote.Extra
import com.dreampany.framework.injector.annote.Favorite
import com.dreampany.framework.injector.annote.PointAnnote
import com.dreampany.framework.injector.annote.StoreAnnote
import com.dreampany.framework.misc.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by roman on 2/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
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
    @Extra
    fun provideExtraSmartMap(): SmartMap<String, Boolean> {
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
    @PointAnnote
    fun providePointSmartCache(): SmartCache<String, Point> {
        return SmartCache.newCache()
    }
}