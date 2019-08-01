package com.dreampany.history.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.frame.data.model.BaseKt
import com.dreampany.frame.misc.Constants
import com.dreampany.frame.util.TimeUtilKt
import com.dreampany.history.data.enums.LinkSource
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-07-24
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@IgnoreExtraProperties
@Entity(
    indices = [Index(
        value = [Constants.Link.ID],
        unique = true
    )],
    primaryKeys = [Constants.Link.ID]
)
data class ImageLink(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    var source: LinkSource? = Constants.Default.NULL,
    var ref: String = Constants.Default.STRING,
    var title: String = Constants.Default.STRING
) : BaseKt() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {

    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {

    }

    constructor(
        id: String,
        source: LinkSource,
        ref: String,
        title: String
    ) : this(time = TimeUtilKt.currentMillis(), id = id, source = source, ref = ref, title = title) {

    }
}