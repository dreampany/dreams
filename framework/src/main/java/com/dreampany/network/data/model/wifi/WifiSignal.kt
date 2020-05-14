package com.dreampany.network.data.model.wifi

import com.dreampany.framework.data.model.BaseParcel
import com.dreampany.network.data.model.wifi.band.WifiBand
import com.dreampany.network.data.model.wifi.band.WifiChannel
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize
import org.apache.commons.lang3.builder.ToStringBuilder

/**
 * Created by roman on 14/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class WifiSignal(
    val primaryFrequency: Int,
    val centerFrequency: Int,
    val width: WifiWidth,
    val band: WifiBand,
    val level: Int,
    val is80211mc: Boolean
) : BaseParcel() {

    override fun hashCode(): Int = Objects.hashCode(primaryFrequency, width)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as WifiSignal
        return Objects.equal(primaryFrequency, item.primaryFrequency) && Objects.equal(width, item.width)
    }

    override fun toString(): String = ToStringBuilder.reflectionToString(this)

    val frequencyStart: Int get() = centerFrequency - width.frequencyWidthHalf
}