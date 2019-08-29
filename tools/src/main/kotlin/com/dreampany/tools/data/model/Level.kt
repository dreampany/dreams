package com.dreampany.tools.data.model

import androidx.room.Ignore
import com.dreampany.frame.data.model.Base
import com.dreampany.frame.util.TimeUtilKt
import com.dreampany.tools.data.enums.LevelType
import com.dreampany.tools.misc.Constants
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-08-29
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class Level(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    var type : LevelType = LevelType.DEFAULT
) : Base() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {

    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {

    }

    constructor(type: LevelType) : this(type.name) {
        this.type = type
    }
}