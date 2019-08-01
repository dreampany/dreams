package com.dreampany.frame.data.model

import androidx.room.Ignore
import com.dreampany.frame.misc.Constants
import com.dreampany.frame.util.TimeUtil
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-07-24
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class Link(
    override var time: Long = Constants.Default.EMPTY_LONG,
    override var id: String = Constants.Default.EMPTY_STRING,
    var title: String = Constants.Default.EMPTY_STRING
) : BaseKt() {

    @Ignore
    constructor() : this(time = TimeUtil.currentTime()) {

    }

    constructor(id: String) : this(time = TimeUtil.currentTime(), id = id) {

    }
}