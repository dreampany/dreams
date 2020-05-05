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
    override suspend fun isFavorite(input: Note): Boolean = mapper.isFavorite(input)

    override suspend fun toggleFavorite(input: Note): Boolean {
        val favorite = isFavorite(input)
        if (favorite) {
            mapper.deleteFavorite(input)
        } else {
            mapper.insertFavorite(input)
        }
        return favorite.not()
    }

    @Throws
    override suspend fun insertItem(input: Note): Long {
        mapper.add(input)
        return dao.insertOrReplace(input)
    }

    @Throws
    override suspend fun insert(inputs: List<Note>): List<Long>? {
        val result = arrayListOf<Long>()
        inputs.forEach { result.add(insertItem(it)) }
        return result
    }

    @Throws
    override suspend fun getNotes(): List<Note>? = mapper.getItems(dao)

    override suspend fun getNotes(ids: List<String>): List<Note>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun getNote(id: String): Note? =
        mapper.getItem(id, this)

    @Throws
    override suspend fun getFavoriteNotes(): List<Note>? = mapper.getFavoriteItems(this)
}