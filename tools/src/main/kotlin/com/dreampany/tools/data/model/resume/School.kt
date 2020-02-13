package com.dreampany.tools.data.model.resume

import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.util.TimeUtilKt
import com.dreampany.tools.misc.Constants
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2020-01-11
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class School(
    @ColumnInfo(name = Constants.Keys.School.TIME)
    override var time: Long = Constants.Default.LONG,
    @ColumnInfo(name = Constants.Keys.School.ID)
    override var id: String = Constants.Default.STRING,
    @ColumnInfo(name = Constants.Keys.School.NAME)
    var name: String? = Constants.Default.NULL,
    @ColumnInfo(name = Constants.Keys.School.LOCATION)
    var location: String? = Constants.Default.NULL,
    @ColumnInfo(name = Constants.Keys.School.DEGREE)
    var degree: String? = Constants.Default.NULL,
    @ColumnInfo(name = Constants.Keys.School.DESCRIPTION)
    var description: String? = Constants.Default.NULL
) : Base() {
    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {

    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {

    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Experience
        return Objects.equal(this.id, item.id)
    }
}