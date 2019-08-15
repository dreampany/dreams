package com.dreampany.tools.data.misc

import com.dreampany.frame.data.enums.Action
import com.dreampany.frame.data.enums.State
import com.dreampany.frame.data.enums.Subtype
import com.dreampany.frame.data.enums.Type
import com.dreampany.frame.data.model.Request
import com.dreampany.tools.data.model.Word
import com.dreampany.tools.misc.Constants

/**
 * Created by Roman-372 on 7/5/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class WordRequest(
    var id: String? = Constants.Default.NULL,
    var source: String? = Constants.Default.NULL,
    var target: String? = Constants.Default.NULL,
    var translate: Boolean = Constants.Default.BOOLEAN,
    var recent: Boolean = Constants.Default.BOOLEAN,
    var history: Boolean = Constants.Default.BOOLEAN,
    type: Type = Type.DEFAULT,
    subtype: Subtype = Subtype.DEFAULT,
    state: State = State.DEFAULT,
    action: Action = Action.DEFAULT,
    input: Word? = Constants.Default.NULL,
    single: Boolean = Constants.Default.BOOLEAN,
    important: Boolean = Constants.Default.BOOLEAN,
    progress: Boolean = Constants.Default.BOOLEAN,
    favorite: Boolean = Constants.Default.BOOLEAN
) : Request<Word>(
    type = type,
    subtype = subtype,
    state = state,
    action = action,
    input = input,
    single = single,
    important = important,
    progress = progress,
    favorite = favorite
) {

}