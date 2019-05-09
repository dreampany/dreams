package com.dreampany.quran.injector.data

import com.dreampany.frame.misc.SmartCache
import com.dreampany.frame.misc.SmartMap
import com.dreampany.quran.data.model.Ayah
import com.dreampany.quran.data.model.Surah
import com.dreampany.quran.misc.AyahAnnote
import com.dreampany.quran.misc.SurahAnnote
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
    @SurahAnnote
    fun provideSurahSmartMap(): SmartMap<Long, Surah> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @SurahAnnote
    fun provideSurahSmartCache(): SmartCache<Long, Surah> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @AyahAnnote
    fun provideAyahSmartMap(): SmartMap<Long, Ayah> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @AyahAnnote
    fun provideAyahSmartCache(): SmartCache<Long, Ayah> {
        return SmartCache.newCache()
    }
}