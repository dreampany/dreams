package com.dreampany.tools.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.frame.data.enums.Subtype
import com.dreampany.frame.data.enums.Type
import com.dreampany.frame.data.model.Base
import com.dreampany.frame.data.model.BaseParcel
import com.dreampany.frame.util.TimeUtilKt
import com.dreampany.tools.misc.Constants
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-08-29
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@Entity(
    indices = [Index(
        value = [Constants.Point.ID, Constants.Point.TYPE, Constants.Point.SUBTYPE],
        unique = true
    )],
    primaryKeys = [Constants.Point.ID, Constants.Point.TYPE, Constants.Point.SUBTYPE]
)
data class Point(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    var type: Type = Type.DEFAULT,
    var subtype: Subtype = Subtype.DEFAULT,
    var credit: Int = Constants.Default.INT
) : Base() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {

    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {

    }
}