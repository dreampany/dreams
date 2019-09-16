package com.dreampany.framework.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.enums.Level
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.misc.Constants
import com.dreampany.framework.util.TimeUtilKt
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
        value = [Constants.Point.ID, Constants.Point.TYPE, Constants.Point.SUBTYPE, Constants.Point.LEVEL],
        unique = true
    )],
    primaryKeys = [Constants.Point.ID, Constants.Point.TYPE, Constants.Point.SUBTYPE, Constants.Point.LEVEL]
)
data class Point(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    var type: Type = Type.DEFAULT,
    var subtype: Subtype = Subtype.DEFAULT,
    var level: Level = Level.DEFAULT,
    var credit: Int = Constants.Default.INT
) : Base() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {

    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {

    }

    constructor(
        id: String,
        type: Type,
        subtype: Subtype,
        level: Level
    ) : this(
        time = TimeUtilKt.currentMillis(),
        id = id,
        type = type,
        subtype = subtype,
        level = level) {
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id, type, subtype, level)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Point
        return Objects.equal(item.id, id) &&
                Objects.equal(item.type, type) &&
                Objects.equal(item.subtype, subtype) &&
                Objects.equal(item.level, level)
    }

    fun hasProperty(type: Type, subtype: Subtype, level: Level): Boolean {
        return (Objects.equal(type, this.type)
                && Objects.equal(subtype, this.subtype)
                && Objects.equal(level, this.level))
    }
}