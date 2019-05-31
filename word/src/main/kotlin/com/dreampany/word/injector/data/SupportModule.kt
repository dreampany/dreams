package com.dreampany.word.injector.data

import com.dreampany.frame.misc.SmartCache
import com.dreampany.frame.misc.SmartMap
import com.dreampany.word.data.model.Word
import com.dreampany.word.misc.WordAnnote
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
    @WordAnnote
    fun provideWordSmartMap(): SmartMap<Long, Word> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @WordAnnote
    fun provideWordSmartCache(): SmartCache<Long, Word> {
        return SmartCache.newCache()
    }
}