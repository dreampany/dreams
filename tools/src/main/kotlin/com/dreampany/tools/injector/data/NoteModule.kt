package com.dreampany.tools.injector.data

import com.dreampany.frame.misc.Room
import com.dreampany.tools.data.misc.NoteMapper
import com.dreampany.tools.data.source.api.NoteDataSource
import com.dreampany.tools.data.source.dao.NoteDao
import com.dreampany.tools.data.source.room.RoomNoteDataSource
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
    @Room
    fun provideRoomNoteDataSource(
        mapper: NoteMapper,
        dao: NoteDao
    ): NoteDataSource {
        return RoomNoteDataSource(mapper, dao)
    }
}