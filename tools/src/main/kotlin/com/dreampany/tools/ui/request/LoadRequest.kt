package com.dreampany.tools.ui.request

import com.dreampany.framework.data.enums.*
import com.dreampany.framework.data.model.Request
import com.dreampany.tools.data.model.Load
import com.dreampany.tools.misc.Constants

/**
 * Created by Roman-372 on 7/5/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class LoadRequest(
    type: Type = Type.DEFAULT,
    subtype: Subtype = Subtype.DEFAULT,
    state: State = State.DEFAULT,
    source: Source = Source.DEFAULT,
    action: Action = Action.DEFAULT,
    single: Boolean = Constants.Default.BOOLEAN,
    important: Boolean = Constants.Default.BOOLEAN,
    progress: Boolean = Constants.Default.BOOLEAN,
    input: Load? = Constants.Default.NULL,
    id: String = Constants.Default.STRING,
    var sourceLang: String? = Constants.Default.NULL,
    var targetLang: String? = Constants.Default.NULL,
    var translate: Boolean = Constants.Default.BOOLEAN,
    var recent: Boolean = Constants.Default.BOOLEAN,
    var history: Boolean = Constants.Default.BOOLEAN,
    var suggests: Boolean = Constants.Default.BOOLEAN
    ) : Request<Load>(
    type = type,
    subtype = subtype,
    state = state,
    source = source,
    action = action,
    single = single,
    important = important,
    progress = progress,
    input = input,
    id = id
) {

}