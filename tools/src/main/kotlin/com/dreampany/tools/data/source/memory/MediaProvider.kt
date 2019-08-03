package com.dreampany.tools.data.source.memory

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.net.Uri
import com.dreampany.frame.util.CursorUtil
import com.dreampany.tools.data.model.Media
import timber.log.Timber

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class MediaProvider<T : Media> {

    protected fun resolveQuery(
        context: Context,
        uri: Uri,
        projection: Array<String>,
        selection: String,
        selectionArgs: Array<String>,
        sortOrder: String): Cursor? {

        try {
            return context.applicationContext.contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)
        } catch (invalidQuery: SQLiteException) {
            Timber.e("ContentProvider makes exception: %s", invalidQuery.toString())
            return null
        }

    }

    open fun getCursor(): Cursor? {
        return null
    }

    open fun getItem(cursor: Cursor?): T? {
        return null
    }

    open fun getItems(limit: Int): List<T>? {
        val cursor = getCursor()
        if (!CursorUtil.hasCursor(cursor)) {
            return null
        }
        var result: MutableList<T>? = null
        if (cursor!!.moveToFirst()) {
            result = mutableListOf()
            do {
                val item = getItem(cursor)
                if (item != null) {
                    result.add(item)
                }
            } while (result.size < limit && cursor.moveToNext())
        }
        CursorUtil.closeCursor(cursor)
        return result
    }

    open fun getItems(): List<T>? {
        return getItems(Integer.MAX_VALUE)
    }

}