package com.dreampany.tools.injector.data

import com.dreampany.framework.misc.Room
import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.tools.data.mapper.ResumeMapper
import com.dreampany.tools.data.model.resume.*
import com.dreampany.tools.data.source.api.ResumeDataSource
import com.dreampany.tools.data.source.room.RoomResumeDataSource
import com.dreampany.tools.data.source.room.dao.ResumeDao
import com.dreampany.tools.injector.annote.*
import com.dreampany.tools.ui.model.resume.*
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

    @Singleton
    @Provides
    @SkillAnnote
    fun provideSkillSmartMap(): SmartMap<String, Skill> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @SkillAnnote
    fun provideSkillSmartCache(): SmartCache<String, Skill> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @SkillItemAnnote
    fun provideSkillItemSmartMap(): SmartMap<String, SkillItem> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @SkillItemAnnote
    fun provideSkillItemSmartCache(): SmartCache<String, SkillItem> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @ExperienceAnnote
    fun provideExperienceSmartMap(): SmartMap<String, Experience> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @ExperienceAnnote
    fun provideExperienceSmartCache(): SmartCache<String, Experience> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @ExperienceItemAnnote
    fun provideExperienceItemSmartMap(): SmartMap<String, ExperienceItem> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @ExperienceItemAnnote
    fun provideExperienceItemSmartCache(): SmartCache<String, ExperienceItem> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @ProjectAnnote
    fun provideProjectSmartMap(): SmartMap<String, Project> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @ProjectAnnote
    fun provideProjectSmartCache(): SmartCache<String, Project> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @ProjectItemAnnote
    fun provideProjectItemSmartMap(): SmartMap<String, ProjectItem> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @ProjectItemAnnote
    fun provideProjectItemSmartCache(): SmartCache<String, ProjectItem> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @SchoolAnnote
    fun provideSchoolSmartMap(): SmartMap<String, School> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @SchoolAnnote
    fun provideSchoolSmartCache(): SmartCache<String, School> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @SchoolItemAnnote
    fun provideSchoolItemSmartMap(): SmartMap<String, SchoolItem> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @SchoolItemAnnote
    fun provideSchoolItemSmartCache(): SmartCache<String, SchoolItem> {
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