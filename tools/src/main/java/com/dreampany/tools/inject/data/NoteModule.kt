package com.dreampany.tools.inject.data

import android.app.Application
import com.dreampany.framework.inject.annote.Room
import com.dreampany.tools.data.source.note.api.NoteDataSource
import com.dreampany.tools.data.source.note.room.NoteRoomDataSource
import com.dreampany.tools.data.source.note.room.dao.NoteDao
import com.dreampany.tools.data.source.note.room.database.DatabaseManager
import com.dreampany.tools.data.source.note.room.mapper.NoteMapper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
class NoteModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): DatabaseManager =
        DatabaseManager.getInstance(application)

    @Provides
    @Singleton
    fun provideNoteDao(database: DatabaseManager): NoteDao = database.noteDao()

    @Singleton
    @Provides
    @Room
    fun provideNoteRoomDataSource(
        mapper: NoteMapper,
        dao: NoteDao
    ): NoteDataSource = NoteRoomDataSource(mapper, dao)
}