package com.dreampany.history.injector.data

import com.dreampany.history.data.model.ImageLink
import com.dreampany.frame.misc.SmartCache
import com.dreampany.frame.misc.SmartMap
import com.dreampany.history.data.model.History
import com.dreampany.history.misc.HistoryAnnote
import com.dreampany.history.misc.HistoryItemAnnote
import com.dreampany.history.misc.ImageLinkAnnote
import com.dreampany.history.misc.ImageLinkItemAnnote
import com.dreampany.history.ui.model.HistoryItem
import com.dreampany.history.ui.model.ImageLinkItem
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
    @HistoryAnnote
    fun provideHistorySmartMap(): SmartMap<String, History> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @HistoryAnnote
    fun provideHistorySmartCache(): SmartCache<String, History> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @HistoryItemAnnote
    fun provideHistoryItemSmartMap(): SmartMap<String, HistoryItem> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @HistoryItemAnnote
    fun provideHistoryItemSmartCache(): SmartCache<String, HistoryItem> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @ImageLinkAnnote
    fun provideImageLinkSmartMap(): SmartMap<String, ImageLink> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @ImageLinkAnnote
    fun provideImageLinkSmartCache(): SmartCache<String, ImageLink> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @ImageLinkItemAnnote
    fun provideImageLinkItemSmartMap(): SmartMap<String, ImageLinkItem> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @ImageLinkItemAnnote
    fun provideImageLinkItemSmartCache(): SmartCache<String, ImageLinkItem> {
        return SmartCache.newCache()
    }
}