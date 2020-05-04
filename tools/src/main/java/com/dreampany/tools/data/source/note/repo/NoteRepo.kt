package com.dreampany.tools.data.source.note.repo

import com.dreampany.framework.inject.annote.Room
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.RxMapper
import com.dreampany.tools.data.model.note.Note
import com.dreampany.tools.data.source.note.api.NoteDataSource
import com.dreampany.tools.data.source.note.pref.NotePref
import com.dreampany.tools.data.source.note.room.mapper.NoteMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 3/21/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class NoteRepo
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    private val pref: NotePref,
    private val mapper: NoteMapper,
    @Room private val room: NoteDataSource
) : NoteDataSource {
    @Throws
    override suspend fun isFavorite(input: Note) = withContext(Dispatchers.IO) {
        room.isFavorite(input)
    }

    @Throws
    override suspend fun toggleFavorite(input: Note) = withContext(Dispatchers.IO) {
        room.toggleFavorite(input)
    }

    @Throws
    override suspend fun insertItem(input: Note) = withContext(Dispatchers.IO) {
        room.insertItem(input)
    }

    @Throws
    override suspend fun insert(inputs: List<Note>): List<Long>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun getNotes(): List<Note>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun getNotes(ids: List<String>): List<Note>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun getNote(id: String) = withContext(Dispatchers.IO) {
        room.getNote(id)
    }

    @Throws
    override suspend fun getFavoriteNotes(
    ) = withContext(Dispatchers.IO) {
        room.getFavoriteNotes()
    }

}