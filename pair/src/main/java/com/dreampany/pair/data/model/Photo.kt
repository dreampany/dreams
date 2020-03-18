package com.dreampany.pair.data.model

import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.dreampany.common.data.model.Base
import com.dreampany.common.misc.constant.Constants
import com.dreampany.common.misc.util.Util
import com.google.common.base.Objects
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 3/18/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@IgnoreExtraProperties
data class Photo(
    @ColumnInfo(name = "photo_time")
    override var time: Long = Constants.Default.LONG,
    @ColumnInfo(name = "photo_url")
    override var id: String = Constants.Default.STRING
) : Base() {

    @Ignore
    constructor() : this(time = Util.currentMillis()) {

    }

    constructor(id: String) : this(time = Util.currentMillis(), id = id) {

    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Photo
        return Objects.equal(item.id, id)
    }
}