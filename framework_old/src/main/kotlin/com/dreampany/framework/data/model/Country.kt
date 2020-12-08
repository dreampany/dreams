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
data class Country(
    var code: String = Constants.Default.STRING, // country code
    var name: String = Constants.Default.STRING,
    var flag: String? = Constants.Default.STRING
) : BaseParcel() {

    override fun hashCode(): Int = Objects.hashCode(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Country
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String = "Country.code:name: $code:$name"
}