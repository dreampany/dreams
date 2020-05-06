package com.dreampany.tools.data.enums.note

import com.dreampany.framework.data.enums.BaseAction
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 26/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class NoteAction : BaseAction {
    DEFAULT, VIEW, ADD, EDIT;

    override val value: String get() = name

    val toState : NoteState get() {
        when(this) {
            VIEW-> return NoteState.VIEWED
            ADD-> return NoteState.ADDED
            EDIT-> return NoteState.EDITED
        }
        return NoteState.DEFAULT
    }
}