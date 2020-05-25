package com.dreampany.tools.data.model.wifi

import com.dreampany.framework.data.model.BaseParcel
import com.dreampany.tools.data.enums.wifi.Strength
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 23/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class Signal(
    val primaryFrequency: Int,
    //val centerFrequency: Int,
    //val width: WifiWidth,
    //val band: Band,
    val level: Int,
    val is80211mc: Boolean
) : BaseParcel() {

    override fun hashCode(): Int = Objects.hashCode(primaryFrequency)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Signal
        return Objects.equal(this.primaryFrequency, item.primaryFrequency)
    }

    override fun toString(): String = "Signal ($primaryFrequency) == $primaryFrequency"

    val strength: Strength
        get() = Strength.calculate(level)
}