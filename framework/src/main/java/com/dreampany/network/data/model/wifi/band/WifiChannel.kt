package com.dreampany.network.data.model.wifi.band

import com.google.common.base.Objects
import org.apache.commons.lang3.builder.CompareToBuilder
import org.apache.commons.lang3.builder.ToStringBuilder

/**
 * Created by roman on 14/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class WifiChannel(val channel: Int = 0, val frequency: Int = 0) : Comparable<WifiChannel> {

    companion object {
        val EMPTY = WifiChannel()
        val FREQUENCY_SPREAD = 5
        val CHANNEL_OFFSET = 2
        var FREQUENCY_OFFSET: Int = FREQUENCY_SPREAD * CHANNEL_OFFSET
        val allowedRange = FREQUENCY_SPREAD / 2
    }

    override fun hashCode(): Int = Objects.hashCode(channel, frequency)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as WifiChannel
        return Objects.equal(channel, item.channel) && Objects.equal(frequency, item.frequency)
    }

    override fun compareTo(other: WifiChannel): Int =
        CompareToBuilder()
            .append(channel, other.channel)
            .append(frequency, other.frequency).toComparison()

    override fun toString(): String = ToStringBuilder.reflectionToString(this)

    fun inRange(frequency: Int): Boolean =
        frequency >= this.frequency - allowedRange && frequency <= this.frequency + allowedRange


}