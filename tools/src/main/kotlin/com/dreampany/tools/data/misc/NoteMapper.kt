package com.dreampany.tools.data.misc

import android.content.Context
import com.dreampany.frame.misc.SmartCache
import com.dreampany.frame.misc.SmartMap
import com.dreampany.frame.util.AndroidUtil
import com.dreampany.frame.util.DataUtilKt
import com.dreampany.frame.util.TimeUtilKt
import com.dreampany.tools.data.model.Note
import com.dreampany.tools.misc.NoteAnnote
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
    val context: Context,
    @NoteAnnote val map: SmartMap<String, Note>,
    @NoteAnnote val cache: SmartCache<String, Note>
) {

    fun isExists(item: Note): Boolean {
        return map.contains(item.id)
    }

    fun toItem(title: String?, description: String?): Note? {
        if (title.isNullOrEmpty() || description.isNullOrEmpty()) {
            return null
        }
        val id = DataUtilKt.getRandId()
        val note = Note(
            time = TimeUtilKt.currentMillis(),
            id = id,
            title = title,
            description = description
        )
        map.put(id, note)
        return note
    }
}