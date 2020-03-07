package com.dreampany.tools.injector.data

import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.tools.data.model.App
import com.dreampany.tools.injector.annote.app.AppAnnote
import com.dreampany.tools.injector.annote.app.AppItemAnnote
import com.dreampany.tools.ui.model.AppItem
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by roman on 7/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
class AppModule {
    @Singleton
    @Provides
    @AppAnnote
    fun provideAppSmartMap(): SmartMap<String, App> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @AppAnnote
    fun provideAppSmartCache(): SmartCache<String, App> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @AppItemAnnote
    fun provideAppItemSmartMap(): SmartMap<String, AppItem> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @AppItemAnnote
    fun provideAppItemSmartCache(): SmartCache<String, AppItem> {
        return SmartCache.newCache()
    }
}