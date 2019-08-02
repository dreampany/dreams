package com.dreampany.history.data.model

import com.dreampany.frame.data.model.Request
import com.dreampany.history.data.enums.HistoryType
import com.dreampany.history.data.enums.HistorySource

/**
 * Created by Roman-372 on 7/26/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class HistoryRequest(
    val source: HistorySource,
    val type: HistoryType,
    val day: Int,
    val month: Int,
    val links: Boolean
) : Request<History>() {

    constructor(
        source: HistorySource,
        type: HistoryType,
        day: Int,
        month: Int,
        links: Boolean,
        important: Boolean,
        progress: Boolean,
        favorite: Boolean
    ) : this(source, type, day, month, links) {
        this.important = important
        this.progress = progress
        this.favorite = favorite
    }
}