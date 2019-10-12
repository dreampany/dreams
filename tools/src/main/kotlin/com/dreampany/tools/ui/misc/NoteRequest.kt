package com.dreampany.tools.ui.misc

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
        val id: String = Constants.Default.STRING,
        val title: String? = Constants.Default.STRING,
        val description: String? = Constants.Default.STRING,
        type: Type = Type.DEFAULT,
        subtype: Subtype = Subtype.DEFAULT,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        input: Note? = Constants.Default.NULL,
        single: Boolean = Constants.Default.BOOLEAN,
        important: Boolean = Constants.Default.BOOLEAN,
        progress: Boolean = Constants.Default.BOOLEAN
) : Request<Note>(
        type = type,
        subtype = subtype,
        state = state,
        action = action,
        input = input,
        single = single,
        important = important,
        progress = progress
) {
}