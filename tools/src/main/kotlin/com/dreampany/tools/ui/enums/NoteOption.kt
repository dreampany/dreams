package com.dreampany.tools.ui.enums

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Roman-372 on 8/5/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class NoteOption : Parcelable {
    EDIT, FAVORITE, ARCHIVE, TRASH, DELETE;

    fun toTitle(): String {
        return name
    }

    companion object {
        fun getAll(): ArrayList<NoteOption> {
            val result = ArrayList<NoteOption>()
            result.add(NoteOption.EDIT)
            result.add(NoteOption.FAVORITE)
            result.add(NoteOption.ARCHIVE)
            result.add(NoteOption.TRASH)
            result.add(NoteOption.DELETE)
            return result
        }
    }
}