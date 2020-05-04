package com.dreampany.tools.data.source.note.room

import com.dreampany.tools.data.model.note.Note
import com.dreampany.tools.data.source.note.api.NoteDataSource
import com.dreampany.tools.data.source.note.room.dao.NoteDao
import com.dreampany.tools.data.source.note.room.mapper.NoteMapper

/**
 * Created by roman on 4/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class NoteRoomDataSource(
    private val mapper: NoteMapper,
    private val dao: NoteDao
) : NoteDataSource {
    override suspend fun isFavorite(input: Note): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFavorite(input: Note): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun insertItem(input: Note): Long {
        TODO("Not yet implemented")
    }

    override suspend fun insert(inputs: List<Note>): List<Long>? {
        TODO("Not yet implemented")
    }

    override suspend fun getNotes(): List<Note>? {
        TODO("Not yet implemented")
    }

    override suspend fun getNotes(ids: List<String>): List<Note>? {
        TODO("Not yet implemented")
    }

    override suspend fun getNote(id: String): Note? {
        TODO("Not yet implemented")
    }

    override suspend fun getFavoriteNotes(): List<Note>? {
        TODO("Not yet implemented")
    }
}