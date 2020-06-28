package com.dreampany.nearby.data.source.memory

import android.database.Cursor
import com.dreampany.framework.misc.exts.has
import com.dreampany.nearby.data.model.media.Media

/**
 * Created by roman on 28/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class MediaProvider<T : Media> {

    open val cursor: Cursor? = null

    open fun get(cursor: Cursor): T? {
        return null
    }

    open fun gets(): List<T>? {
        val cursor = cursor ?: return null
        if (!cursor.has) {
            return null
        }
        var result: MutableList<T>? = null
        if (cursor.moveToFirst()) {
            result = mutableListOf()
            do {
                val item = get(cursor)
                if (item != null) {
                    result.add(item)
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        return result
    }
}