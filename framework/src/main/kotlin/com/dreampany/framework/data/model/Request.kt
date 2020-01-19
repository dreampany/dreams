package com.dreampany.framework.data.model

import com.dreampany.framework.data.enums.*
import com.dreampany.framework.misc.Constants

/**
 * Created by Roman-372 on 7/5/2019
 * Copyright (c) 2019 bjit. All rights reserved.
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
    var id: String? = Constants.Default.NULL,
    var ids: List<String>? = Constants.Default.NULL,
    var input: T? = Constants.Default.NULL,
    var inputs: List<T>? = Constants.Default.NULL
) {
}