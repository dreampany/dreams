package com.dreampany.tools.data.enums.wifi

import com.dreampany.framework.data.model.BaseParcel

/**
 * Created by roman on 16/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BandGHZ(val range: Pair<Int, Int>, val channels: List<Pair<Channel, Channel>>) : BaseParcel() {

    companion object {
        const val FREQUENCY_SPREAD = 5
        const val CHANNEL_OFFSET = 2
        const val FREQUENCY_OFFSET = FREQUENCY_SPREAD * CHANNEL_OFFSET
    }

    val first: Channel get() = channels.first().first
    val last: Channel get() = channels.last().second

    fun inRange(frequency: Int): Boolean =
        frequency >= range.first && frequency <= range.second

    fun channelByFrequency(frequency: Int): Channel {
        var channel: Pair<Channel, Channel>? = null
        if (inRange(frequency)) {
            channel = channels.find { Channel.EMPTY.equals(channel(frequency, it)).not() }
        }
        return if (channel == null) Channel.EMPTY else channel(frequency, channel)
    }

    fun channelByChannel(channel: Int): Channel {
        val found = channels.find { channel >= it.first.channel && channel <= it.second.channel }
        return if (found == null) Channel.EMPTY else Channel(
            channel,
            found.first.frequency + ((channel - found.first.channel) * FREQUENCY_SPREAD)
        )
    }

    fun channel(frequency: Int, channel: Pair<Channel, Channel>): Channel {
        val first = channel.first
        val last = channel.second
        val channel: Int =
            (((frequency - first.frequency).toDouble() / FREQUENCY_SPREAD) + first.channel + 0.5).toInt()
        return if (channel >= first.channel && channel <= last.channel)
            Channel(channel, frequency)
        else Channel.EMPTY
    }


}