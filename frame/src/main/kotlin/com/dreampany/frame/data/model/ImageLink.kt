package com.dreampany.frame.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.frame.misc.Constants
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
    override var time: Long = Constants.Default.EMPTY_LONG,
    override var id: String = Constants.Default.EMPTY_STRING,
    var ref: String = Constants.Default.EMPTY_STRING,
    var url: String = Constants.Default.EMPTY_STRING,
    var title: String = Constants.Default.EMPTY_STRING
) : BaseKt() {

    @Ignore
    constructor() : this(time = 0) {

    }
}