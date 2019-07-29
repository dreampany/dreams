package com.dreampany.history.data.model

import com.dreampany.frame.data.model.Request
import com.dreampany.history.data.enums.HistoryType

/**
 * Created by Roman-372 on 7/26/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class HistoryRequest(val type: HistoryType, val day: Int, val month: Int) :
    Request<History>() {
    constructor(
        type: HistoryType,
        day: Int,
        month: Int,
        important: Boolean,
        progress: Boolean,
        favorite: Boolean,
        notify: Boolean = false
    ) : this(type, day, month) {
        this.important = important
        this.progress = progress
        this.favorite = favorite
    }
}