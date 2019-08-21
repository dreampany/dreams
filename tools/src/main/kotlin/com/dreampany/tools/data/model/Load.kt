package com.dreampany.tools.data.model

import androidx.room.Ignore
import com.dreampany.frame.data.model.Base
import com.dreampany.frame.util.TimeUtilKt
import com.dreampany.tools.misc.Constants
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by Roman-372 on 8/19/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class Load(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    var current: Int = Constants.Default.INT,
    var total: Int = Constants.Default.INT
) : Base() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {

    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Note
        return Objects.equal(this.id, item.id)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }
}