package com.dreampany.tools.ui.misc

import com.dreampany.framework.data.enums.*
import com.dreampany.framework.data.model.Request
import com.dreampany.tools.data.model.Station
import com.dreampany.tools.misc.Constants

/**
 * Created by roman on 2019-10-12
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class StationRequest(
    val id: String? = Constants.Default.NULL,
    val countryCode: String? = Constants.Default.NULL,
    type: Type = Type.DEFAULT,
    subtype: Subtype = Subtype.DEFAULT,
    state: State = State.DEFAULT,
    source: Source = Source.DEFAULT,
    action: Action = Action.DEFAULT,
    single: Boolean = Constants.Default.BOOLEAN,
    important: Boolean = Constants.Default.BOOLEAN,
    progress: Boolean = Constants.Default.BOOLEAN,
    input: Station? = Constants.Default.NULL,
    limit: Long = Constants.Default.LONG
) : Request<Station>(
    type = type,
    subtype = subtype,
    state = state,
    source = source,
    action = action,
    single = single,
    important = important,
    progress = progress,
    input = input,
    limit = limit
) {

}