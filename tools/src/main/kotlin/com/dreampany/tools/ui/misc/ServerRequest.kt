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
    var id: String? = Constants.Default.NULL,
    type: Type = Type.DEFAULT,
    subtype: Subtype = Subtype.DEFAULT,
    state: State = State.DEFAULT,
    source: Source = Source.DEFAULT,
    action: Action = Action.DEFAULT,
    input: Server? = Constants.Default.NULL,
    single: Boolean = Constants.Default.BOOLEAN,
    important: Boolean = Constants.Default.BOOLEAN,
    progress: Boolean = Constants.Default.BOOLEAN
) : Request<Server>(
    type = type,
    subtype = subtype,
    state = state,
    source = source,
    action = action,
    input = input,
    single = single,
    important = important,
    progress = progress
) {


}
