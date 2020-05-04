package com.dreampany.tools.data.source.note.api

import com.dreampany.tools.data.model.note.Note

/**
 * Created by roman on 4/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface NoteDataSource {
    @Throws
    suspend fun isFavorite(input: Note): Boolean

    @Throws
    suspend fun toggleFavorite(input: Note): Boolean

    @Throws
    suspend fun insertItem(input: Note): Long

    @Throws
    suspend fun insert(inputs: List<Note>): List<Long>?

    @Throws
    suspend fun getNotes(): List<Note>?

    @Throws
    suspend fun getNote(id: String): Note?

    @Throws
    suspend fun getNotes(ids: List<String>): List<Note>?

    @Throws
    suspend fun getFavoriteNotes(): List<Note>?
}