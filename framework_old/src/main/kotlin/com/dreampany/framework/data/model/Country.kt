package com.dreampany.framework.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.misc.Constants
import com.dreampany.framework.util.TimeUtilKt
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-10-06
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@Entity(
    indices = [Index(
        value = [Constants.Country.ID],
        unique = true
    )],
    primaryKeys = [Constants.Country.ID]
)
data class Country(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING, // country code
    var name: String? = Constants.Default.NULL,
    var capital: String? = Constants.Default.NULL,
    var latitude: Double = Constants.Default.DOUBLE,
    var longiitude: Double = Constants.Default.DOUBLE
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
        val item = other as Country
        return Objects.equal(this.id, item.id)
    }
}