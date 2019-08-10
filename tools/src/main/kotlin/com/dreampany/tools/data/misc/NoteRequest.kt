package com.dreampany.tools.data.misc

import com.dreampany.frame.data.enums.Action
import com.dreampany.frame.data.model.Request
import com.dreampany.tools.data.enums.NoteState
import com.dreampany.tools.data.enums.NoteType
import com.dreampany.tools.data.model.Note
import com.dreampany.tools.misc.Constants

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class NoteRequest(
    val type: NoteType = NoteType.DEFAULT,
    val state: NoteState = NoteState.DEFAULT,
    val id: String? = Constants.Default.NULL,
    val title: String? = Constants.Default.NULL,
    val description: String? = Constants.Default.NULL,
    action: Action = Action.DEFAULT,
    input: Note? = Constants.Default.NULL,
    single: Boolean = Constants.Default.BOOLEAN,
    important: Boolean = Constants.Default.BOOLEAN,
    progress: Boolean = Constants.Default.BOOLEAN,
    favorite: Boolean = Constants.Default.BOOLEAN
) : Request<Note>(
    action = action,
    input = input,
    single = single,
    important = important,
    progress = progress,
    favorite = favorite
) {
}