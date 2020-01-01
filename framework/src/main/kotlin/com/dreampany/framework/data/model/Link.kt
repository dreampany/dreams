package com.dreampany.framework.data.model

import androidx.room.Ignore
import com.dreampany.framework.misc.Constants
import com.dreampany.framework.util.TimeUtilKt
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-07-24
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class Link(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    var title: String = Constants.Default.STRING
) : Base() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {

    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {

    }

    constructor(id: String, title: String) : this(time = TimeUtilKt.currentMillis(), id = id, title = title) {

    }
}