package com.dreampany.tools.data.source.note.room.mapper

import com.dreampany.framework.data.source.mapper.StoreMapper
import com.dreampany.framework.data.source.repo.StoreRepo
import com.dreampany.framework.misc.exts.randomId
import com.dreampany.framework.misc.exts.value
import com.dreampany.tools.data.enums.note.NoteState
import com.dreampany.tools.data.enums.note.NoteSubtype
import com.dreampany.tools.data.enums.note.NoteType
import com.dreampany.tools.data.model.note.Note
import com.dreampany.tools.data.source.note.api.NoteDataSource
import com.dreampany.tools.data.source.note.pref.NotePref
import com.dreampany.tools.data.source.note.room.dao.NoteDao
import com.google.common.collect.Maps
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class NoteMapper
@Inject constructor(
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepo,
    private val pref: NotePref
) {
    private val notes: MutableMap<String, Note>
    private val favorites: MutableMap<String, Boolean>

    init {
        notes = Maps.newConcurrentMap()
        favorites = Maps.newConcurrentMap()
    }

    @Synchronized
    fun add(input: Note) = notes.put(input.id, input)

    @Synchronized
    fun getItem(id: String?, title: String, description: String?): Note? {
        val id = id ?: randomId()
        var note = notes.get(id)
        if (note == null) {
            note = Note(id)
            notes.put(id, note)
        }
        note.title = title
        note.description = description
        return note
    }

    @Synchronized
    suspend fun getItem(id: String, source: NoteDataSource): Note? {
        updateCache(source)
        val result = notes.get(id)
        return result
    }

    @Throws
    suspend fun isFavorite(input: Note): Boolean {
        if (!favorites.containsKey(input.id)) {
            val favorite = storeRepo.isExists(
                input.id,
                NoteType.NOTE.value,
                NoteSubtype.DEFAULT.value,
                NoteState.FAVORITE.value
            )
            favorites.put(input.id, favorite)
        }
        return favorites.get(input.id).value
    }

    @Throws
    suspend fun insertFavorite(input: Note): Boolean {
        favorites.put(input.id, true)
        val store = storeMapper.getItem(
            input.id,
            NoteType.NOTE.value,
            NoteSubtype.DEFAULT.value,
            NoteState.FAVORITE.value
        )
        store?.let { storeRepo.insert(it) }
        return true
    }

    @Throws
    suspend fun deleteFavorite(input: Note): Boolean {
        favorites.put(input.id, false)
        val store = storeMapper.getItem(
            input.id,
            NoteType.NOTE.value,
            NoteSubtype.DEFAULT.value,
            NoteState.FAVORITE.value
        )
        store?.let { storeRepo.delete(it) }
        return false
    }

    @Throws
    @Synchronized
    fun getItems(
        dao: NoteDao
    ): List<Note>? {
        updateCache(dao)
        return ArrayList(notes.values)
    }

    @Throws
    @Synchronized
    suspend fun getFavoriteItems(
        source: NoteDataSource
    ): List<Note>? {
        updateCache(source)
        val stores = storeRepo.getStores(
            NoteType.NOTE.value,
            NoteSubtype.DEFAULT.value,
            NoteState.FAVORITE.value
        )
        val outputs = stores?.mapNotNull { input -> notes.get(input.id) }
        var result: List<Note>? = null
        /*outputs?.let {
            result = sortedCoins(it, currency, sort, sortDirection)
        }*/
        result?.forEach {
            //bindQuote(currency, it, quoteDao)
        }
        return outputs
    }

    @Throws
    @Synchronized
    private suspend fun updateCache(source: NoteDataSource) {
        if (notes.isEmpty()) {
            source.getNotes()?.let {
                if (it.isNotEmpty())
                    it.forEach { add(it) }
            }
        }
    }

    @Throws
    @Synchronized
    private fun updateCache(dao: NoteDao) {
        if (notes.isEmpty()) {
            dao.items?.let {
                if (it.isNotEmpty())
                    it.forEach { add(it) }
            }
        }
    }
}