package com.dreampany.tools.ui.request

import com.dreampany.framework.data.enums.*
import com.dreampany.framework.data.model.Request
import com.dreampany.tools.data.model.word.Word
import com.dreampany.tools.misc.Constants

/**
 * Created by Roman-372 on 7/5/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class WordRequest(
    type: Type = Type.DEFAULT,
    subtype: Subtype = Subtype.DEFAULT,
    state: State = State.DEFAULT,
    action: Action = Action.DEFAULT,
    source: Source = Source.DEFAULT,
    single: Boolean = Constants.Default.BOOLEAN,
    important: Boolean = Constants.Default.BOOLEAN,
    progress: Boolean = Constants.Default.BOOLEAN,
    limit: Long = Constants.Default.LONG,
    id: String? = Constants.Default.NULL,
    input: Word? = Constants.Default.NULL,
    val sourceLang: String? = Constants.Default.NULL,
    val targetLang: String? = Constants.Default.NULL,
    val translate: Boolean = Constants.Default.BOOLEAN,
    val recent: Boolean = Constants.Default.BOOLEAN,
    val history: Boolean = Constants.Default.BOOLEAN,
    val suggests: Boolean = Constants.Default.BOOLEAN
) : Request<Word>(
    type = type,
    subtype = subtype,
    state = state,
    action = action,
    source = source,
    single = single,
    important = important,
    progress = progress,
    limit = limit,
    input = input,
    id = id
) {

}