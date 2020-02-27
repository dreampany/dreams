package com.dreampany.tools.injector.data

import com.dreampany.framework.injector.annote.Room
import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.tools.data.mapper.ContactMapper
import com.dreampany.tools.data.model.Contact
import com.dreampany.tools.data.source.api.ContactDataSource
import com.dreampany.tools.data.source.room.RoomContactDataSource
import com.dreampany.tools.data.source.room.dao.ContactDao
import com.dreampany.tools.injector.annote.ContactAnnote
import com.dreampany.tools.injector.annote.ContactItemAnnote
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
    fun provideContactSmartCache(): SmartCache<String, Contact> {
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
    fun provideRoomContactDataSource(
        mapper: ContactMapper,
        dao: ContactDao
    ): ContactDataSource {
        return RoomContactDataSource(mapper, dao)
    }
}