package com.dreampany.tools.injector.data

import com.dreampany.framework.misc.Room
import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.tools.data.mapper.ResumeMapper
import com.dreampany.tools.data.model.Profile
import com.dreampany.tools.data.model.Resume
import com.dreampany.tools.data.source.api.ResumeDataSource
import com.dreampany.tools.data.source.room.RoomResumeDataSource
import com.dreampany.tools.data.source.room.dao.ResumeDao
import com.dreampany.tools.injector.annote.ProfileAnnote
import com.dreampany.tools.injector.annote.ProfileItemAnnote
import com.dreampany.tools.injector.annote.ResumeAnnote
import com.dreampany.tools.injector.annote.ResumeItemAnnote
import com.dreampany.tools.ui.model.ProfileItem
import com.dreampany.tools.ui.model.resume.ResumeItem
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by roman on 2020-01-12
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
class ResumeModule {

    @Singleton
    @Provides
    @ResumeAnnote
    fun provideResumeSmartMap(): SmartMap<String, Resume> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @ResumeAnnote
    fun provideResumeSmartCache(): SmartCache<String, Resume> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @ResumeItemAnnote
    fun provideResumeItemSmartMap(): SmartMap<String, ResumeItem> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @ResumeItemAnnote
    fun provideResumeItemSmartCache(): SmartCache<String, ResumeItem> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @ProfileAnnote
    fun provideProfileSmartMap(): SmartMap<String, Profile> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @ProfileAnnote
    fun provideProfileSmartCache(): SmartCache<String, Profile> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @ProfileItemAnnote
    fun provideProfileItemSmartMap(): SmartMap<String, ProfileItem> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @ProfileItemAnnote
    fun provideProfileItemSmartCache(): SmartCache<String, ProfileItem> {
        return SmartCache.newCache()
    }

    @Room
    @Provides
    @Singleton
    fun provideRoomResumeDataSource(
        mapper: ResumeMapper,
        dao: ResumeDao
    ): ResumeDataSource {
        return RoomResumeDataSource(mapper, dao)
    }
}