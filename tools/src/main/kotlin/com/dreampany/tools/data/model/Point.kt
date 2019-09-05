package com.dreampany.tools.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.util.TimeUtilKt
import com.dreampany.tools.misc.Constants
import com.google.common.base.Objects
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Point
        return Objects.equal(item.id, id) &&
                Objects.equal(item.type, type) &&
                Objects.equal(item.subtype, subtype)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id, type, subtype)
    }
}