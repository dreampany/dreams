package com.dreampany.tools.injector.data

import com.dreampany.framework.injector.annote.Room
import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.tools.data.mapper.NoteMapper
import com.dreampany.tools.data.model.Note
import com.dreampany.tools.data.source.api.NoteDataSource
import com.dreampany.tools.data.source.room.dao.NoteDao
import com.dreampany.tools.data.source.room.RoomNoteDataSource
import com.dreampany.tools.injector.annote.NoteAnnote
import com.dreampany.tools.injector.annote.NoteItemAnnote
import com.dreampany.tools.ui.model.NoteItem
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
class NoteModule {

    @Singleton
    @Provides
    @NoteAnnote
    fun provideNoteSmartMap(): SmartMap<String, Note> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @NoteAnnote
    fun provideNoteSmartCache(): SmartCache<String, Note> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @NoteItemAnnote
    fun provideNoteItemSmartMap(): SmartMap<String, NoteItem> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @NoteItemAnnote
    fun provideNoteItemSmartCache(): SmartCache<String, NoteItem> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @Room
    fun provideRoomNoteDataSource(
        mapper: NoteMapper,
        dao: NoteDao
    ): NoteDataSource {
        return RoomNoteDataSource(mapper, dao)
    }
}