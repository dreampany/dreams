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
    @ImageAnnote
    fun provideImageSmartMap(): SmartMap<Long, Image> {
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
    fun provideImageSmartCache(): SmartCache<Long, Image> {
        return SmartCache.newCache()
    }


    /*
  @Singleton
    @Provides
    @ApkAnnote
    fun provideApkSmartMap(): SmartMap<String, Apk> {
        return SmartMap()
    }

    @Singleton
       @Provides
       @ImageAnnote
       fun provideImageSmartMap(): SmartMap<String, Image> {
           return SmartMap()
       }

       @Singleton
       @Provides
       @AudioAnnote
       fun provideAudioSmartMap(): SmartMap<String, Audio> {
           return SmartMap()
       }

       @Singleton
       @Provides
       @VideoAnnote
       fun provideVideoSmartMap(): SmartMap<String, Video> {
           return SmartMap()
       }

       @Singleton
       @Provides
       @DocumentAnnote
       fun provideDocumentSmartMap(): SmartMap<String, Document> {
           return SmartMap()
       }

       @Singleton
       @Provides
       @FileAnnote
       fun provideFileSmartMap(): SmartMap<String, File> {
           return SmartMap()
       }*/


    /*
    @Singleton
    @Provides
    @ApkAnnote
    fun provideApkSmartCache(): SmartCache<String, Apk> {
        return SmartCache()
    }

  @Singleton
       @Provides
       @ImageAnnote
       fun provideImageSmartCache(): SmartCache<String, Image> {
           return SmartCache()
       }

       @Singleton
       @Provides
       @AudioAnnote
       fun provideAudioSmartCache(): SmartCache<String, Audio> {
           return SmartCache()
       }

       @Singleton
       @Provides
       @VideoAnnote
       fun provideVideoSmartCache(): SmartCache<String, Video> {
           return SmartCache()
       }

       @Singleton
       @Provides
       @DocumentAnnote
       fun provideDocumentSmartCache(): SmartCache<String, Document> {
           return SmartCache()
       }

       @Singleton
       @Provides
       @FileAnnote
       fun provideFileSmartCache(): SmartCache<String, File> {
           return SmartCache()
       }*/
}