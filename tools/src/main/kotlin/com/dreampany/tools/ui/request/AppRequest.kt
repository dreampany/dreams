package com.dreampany.tools.ui.request

import com.dreampany.framework.data.enums.*
import com.dreampany.framework.data.model.Request
import com.dreampany.tools.data.enums.AppType
import com.dreampany.tools.data.model.App
import com.dreampany.tools.data.model.question.Question
import com.dreampany.tools.misc.Constants

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class AppRequest(
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
    ids: List<String>? = Constants.Default.NULL,
    input: App? = Constants.Default.NULL,
    inputs: List<App>? = Constants.Default.NULL,
    var lockStatus: Boolean = Constants.Default.BOOLEAN,
    var appType: AppType = AppType.DEFAULT
) : Request<App>(
    type = type,
    subtype = subtype,
    state = state,
    action = action,
    source = source,
    single = single,
    important = important,
    progress = progress,
    limit = limit,
    id = id,
    ids = ids,
    input = input,
    inputs = inputs
) {
}