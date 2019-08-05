package com.dreampany.tools.injector.data

import com.dreampany.tools.data.model.Demo
import com.dreampany.tools.misc.DemoAnnote
import com.dreampany.frame.misc.SmartCache
import com.dreampany.frame.misc.SmartMap
import com.dreampany.tools.data.model.Apk
import com.dreampany.tools.data.model.Note
import com.dreampany.tools.misc.ApkAnnote
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
    fun provideDemoSmartMap(): SmartMap<String, Demo> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @DemoAnnote
    fun provideDemoSmartCache(): SmartCache<String, Demo> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @ApkAnnote
    fun provideApkSmartMap(): SmartMap<String, Apk> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @ApkAnnote
    fun provideApkSmartCache(): SmartCache<String, Apk> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @ApkAnnote
    fun provideNoteSmartMap(): SmartMap<String, Note> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @ApkAnnote
    fun provideNoteSmartCache(): SmartCache<String, Note> {
        return SmartCache.newCache()
    }
}