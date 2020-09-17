package com.dreampany.tools.data.model.wifi

import com.dreampany.framework.data.model.BaseParcel
import com.dreampany.tools.data.enums.wifi.Band
import com.dreampany.tools.data.enums.wifi.Channel
import com.dreampany.tools.data.enums.wifi.Strength
import com.dreampany.tools.data.enums.wifi.Width
import com.dreampany.tools.misc.utils.WifiUtils
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize
import org.apache.commons.lang3.builder.ToStringBuilder
import java.util.*

/**
 * Created by roman on 23/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class Signal(
    val primaryFrequency: Int,
    val centerFrequency: Int,
    val width: Width,
    val band: Band,
    val level: Int,
    val is80211mc: Boolean
) : BaseParcel() {

    companion object {
        const val FREQUENCY_UNITS = "MHz"
    }

    override fun hashCode(): Int = Objects.hashCode(primaryFrequency, width)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Signal
        return Objects.equal(this.primaryFrequency, item.primaryFrequency)
                && Objects.equal(this.width, item.width)
    }

    override fun toString(): String = ToStringBuilder.reflectionToString(this)

    val strength: Strength
        get() = Strength.calculate(level)

    val distance: Double
        get() = WifiUtils.calculateDistance(primaryFrequency, level)

    val channelDisplay: String
        get() {
            val primaryChannel = this.primaryChannel.channel
            val centerChannel = this.centerChannel.channel
            var channel = primaryChannel.toString()
            if (primaryChannel != centerChannel) {
                channel += "($centerChannel)"
            }
            return channel
        }

    val primaryChannel: Channel
        get() = band.band.channelByFrequency(primaryFrequency)

    val centerChannel: Channel
        get() = band.band.channelByFrequency(centerFrequency)
}