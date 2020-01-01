package com.dreampany.simple.data.model

import com.dreampany.simple.data.enums.*
import com.dreampany.simple.misc.Constants

/**
 * Created by roman on 2020-01-01
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class Request<T>(
    var type: Type = Type.DEFAULT,
    var subtype: Subtype = Subtype.DEFAULT,
    var state: State = State.DEFAULT,
    var action: Action = Action.DEFAULT,
    var source: Source = Source.DEFAULT,
    val single: Boolean = Constants.Default.BOOLEAN,
    var important: Boolean = Constants.Default.BOOLEAN,
    var progress: Boolean = Constants.Default.BOOLEAN,
    var start: Long = Constants.Default.LONG,
    var limit: Long = Constants.Default.LONG,
    var input: T? = Constants.Default.NULL,
    var id: String? = Constants.Default.NULL,
    var ids: List<String>? = Constants.Default.NULL
) {
}