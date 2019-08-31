package com.dreampany.tools.data.misc

import android.content.Context
import com.dreampany.frame.data.model.Store
import com.dreampany.frame.misc.SmartCache
import com.dreampany.frame.misc.SmartMap
import com.dreampany.frame.util.DataUtilKt
import com.dreampany.tools.data.model.Note
import com.dreampany.tools.data.source.api.NoteDataSource
import com.dreampany.tools.misc.NoteAnnote
import com.dreampany.tools.misc.NoteItemAnnote
import com.dreampany.tools.ui.model.NoteItem
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class NoteMapper
@Inject constructor(
    private val context: Context,
    @NoteAnnote private val map: SmartMap<String, Note>,
    @NoteAnnote private val cache: SmartCache<String, Note>,
    @NoteItemAnnote private val uiMap: SmartMap<String, NoteItem>,
    @NoteItemAnnote private val uiCache: SmartCache<String, NoteItem>
) {

    fun isExists(item: Note): Boolean {
        return map.contains(item.id)
    }

    fun getUiItem(id: String): NoteItem? {
        return uiMap.get(id)
    }

    fun putUiItem(id: String, uiItem: NoteItem) {
        uiMap.put(id, uiItem)
    }

    fun getItem(id: String?, title: String?, description: String?): Note? {
        if (title.isNullOrEmpty() || description.isNullOrEmpty()) {
            return null
        }
        val id = id ?: DataUtilKt.getRandId()
        var note = map.get(id)
        if (note == null) {
            note = Note(id)
            map.put(id, note)
        }
        note.title = title
        note.description = description
        return note
    }

    fun getItem(input: Store, source: NoteDataSource): Note? {
        var out: Note? = map.get(input.id)
        if (out == null) {
            out = source.getItem(input.id)
            map.put(input.id, out)
        }
        return out
    }
}