package com.dreampany.wifi.data.model

import android.os.Parcelable
import com.dreampany.wifi.data.model.band.WifiBand
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
) : Parcelable {

    companion object {
        fun getBand(frequency: Int): WifiBand =
            WifiBand.values().find { it.channels.inRange(frequency) } ?: WifiBand.GHZ2
    }

    constructor(
        primaryFrequency: Int,
        centerFrequency: Int,
        width: WifiWidth,
        level: Int,
        is80211mc: Boolean
    ) : this(
        primaryFrequency,
        centerFrequency,
        width,
        getBand(primaryFrequency),
        level,
        is80211mc
    )

    override fun hashCode(): Int = Objects.hashCode(primaryFrequency, width)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as WifiSignal
        return Objects.equal(primaryFrequency, item.primaryFrequency) &&
                Objects.equal(width, item.width)
    }

    override fun toString(): String = ToStringBuilder.reflectionToString(this)
    val frequencyStart: Int get() = centerFrequency - width.frequencyWidthHalf
    val frequencyEnd: Int get() = centerFrequency + width.frequencyWidthHalf
}