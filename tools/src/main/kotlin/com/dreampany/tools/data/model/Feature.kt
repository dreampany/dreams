package com.dreampany.tools.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.frame.data.enums.Type
import com.dreampany.frame.data.model.Base
import com.dreampany.frame.util.TimeUtilKt
import com.dreampany.tools.misc.Constants
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@Entity(
    indices = [Index(
        value = [Constants.Feature.ID],
        unique = true
    )],
    primaryKeys = [Constants.Feature.ID]
)
data class Feature(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING, // package
    var type: Type = Type.DEFAULT
) : Base() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {

    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {

    }

    constructor(id: String, type: Type) : this(
        time = TimeUtilKt.currentMillis(),
        id = id,
        type = type
    ) {

    }

    constructor(type: Type) : this(
        id = type.name,
        type = type
    ) {

    }


}