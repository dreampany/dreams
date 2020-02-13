package com.dreampany.tools.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.tools.misc.Constants
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.util.TimeUtilKt
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-08-02
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@IgnoreExtraProperties
@Entity(
    indices = [Index(
        value = [Constants.Keys.Demo.ID],
        unique = true
    )],
    primaryKeys = [Constants.Keys.Demo.ID]
)
data class Demo(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING
) : Base() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {

    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {

    }
}