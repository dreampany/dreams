package com.dreampany.translation.injector

import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.translation.data.model.TextTranslation
import com.dreampany.translation.misc.TextTranslateAnnote
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Roman-372 on 7/4/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
class SupportModule {

    @Singleton
    @Provides
    @TextTranslateAnnote
    fun provideTextTranslateSmartMap(): SmartMap<String, TextTranslation> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @TextTranslateAnnote
    fun provideTextTranslateSmartCache(): SmartCache<String, TextTranslation> {
        return SmartCache.newCache()
    }
}