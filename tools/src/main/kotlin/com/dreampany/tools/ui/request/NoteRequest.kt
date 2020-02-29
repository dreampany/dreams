package com.dreampany.tools.ui.request

import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.model.Request
import com.dreampany.tools.data.model.Note
import com.dreampany.tools.misc.Constants

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class NoteRequest(
    type: Type = Type.DEFAULT,
    subtype: Subtype = Subtype.DEFAULT,
    state: State = State.DEFAULT,
    action: Action = Action.DEFAULT,
    single: Boolean = Constants.Default.BOOLEAN,
    important: Boolean = Constants.Default.BOOLEAN,
    progress: Boolean = Constants.Default.BOOLEAN,
    id: String? = Constants.Default.NULL,
    input: Note? = Constants.Default.NULL,
    val title: String? = Constants.Default.NULL,
    val description: String? = Constants.Default.NULL
) : Request<Note>(
    type = type,
    subtype = subtype,
    state = state,
    action = action,
    single = single,
    important = important,
    progress = progress,
    input = input,
    id = id
) {
}