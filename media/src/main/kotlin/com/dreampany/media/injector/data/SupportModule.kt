package com.dreampany.media.injector.data

import com.dreampany.frame.misc.SmartCache
import com.dreampany.frame.misc.SmartMap
import com.dreampany.media.data.model.Apk
import com.dreampany.media.data.model.Image
import com.dreampany.media.misc.ApkAnnote
import com.dreampany.media.misc.ImageAnnote
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
    @ApkAnnote
    fun provideApkSmartMap(): SmartMap<Long, Apk> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @ApkAnnote
    fun provideApkSmartCache(): SmartCache<Long, Apk> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @ImageAnnote
    fun provideImageSmartMap(): SmartMap<Long, Image> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @ImageAnnote
    fun provideImageSmartCache(): SmartCache<Long, Image> {
        return SmartCache.newCache()
    }
}