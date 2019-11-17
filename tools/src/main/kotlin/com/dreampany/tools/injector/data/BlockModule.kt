package com.dreampany.tools.injector.data

import com.dreampany.framework.misc.Room
import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.tools.data.mapper.CoinMapper
import com.dreampany.tools.data.model.Coin
import com.dreampany.tools.data.model.Contact
import com.dreampany.tools.data.source.api.CoinDataSource
import com.dreampany.tools.data.source.room.RoomCoinDataSource
import com.dreampany.tools.data.source.room.dao.CoinDao
import com.dreampany.tools.data.source.room.dao.QuoteDao
import com.dreampany.tools.injector.annotation.ContactAnnote
import com.dreampany.tools.injector.annotation.ContactItemAnnote
import com.dreampany.tools.ui.model.ContactItem
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by roman on 2019-11-12
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
class BlockModule {

    @Singleton
    @Provides
    @ContactAnnote
    fun provideContactSmartMap(): SmartMap<String, Contact> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @ContactAnnote
    fun provideContactSmartCache(): SmartCache<String, Coin> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @ContactItemAnnote
    fun provideContactItemSmartMap(): SmartMap<String, ContactItem> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @ContactItemAnnote
    fun provideContactItemSmartCache(): SmartCache<String, ContactItem> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @Room
    fun provideRoomCoinDataSource(
        mapper: CoinMapper,
        dao: CoinDao,
        quoteDao: QuoteDao
    ): CoinDataSource {
        return RoomCoinDataSource(mapper, dao, quoteDao)
    }
}