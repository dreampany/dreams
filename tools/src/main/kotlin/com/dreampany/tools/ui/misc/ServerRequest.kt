package com.dreampany.tools.ui.misc

import com.dreampany.framework.data.enums.*
import com.dreampany.framework.data.model.Request
import com.dreampany.tools.data.model.Server
import com.dreampany.tools.misc.Constants

/**
 * Created by roman on 2019-10-08
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ServerRequest(
    type: Type = Type.DEFAULT,
    subtype: Subtype = Subtype.DEFAULT,
    state: State = State.DEFAULT,
    action: Action = Action.DEFAULT,
    source: Source = Source.DEFAULT,
    single: Boolean = Constants.Default.BOOLEAN,
    important: Boolean = Constants.Default.BOOLEAN,
    progress: Boolean = Constants.Default.BOOLEAN,
    limit: Long = Constants.Default.LONG,
    input: Server? = Constants.Default.NULL,
    id: String? = Constants.Default.NULL
) : Request<Server>(
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
