package com.dreampany.wifi.data.model.band

import android.os.Parcelable
import kotlin.math.roundToInt

/**
 * Created by roman on 14/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class WifiChannels(
    private val range: Pair<Int, Int>,
    private val channels: List<Pair<WifiChannel, WifiChannel>>
) : Parcelable {
    fun inRange(frequency: Int): Boolean =
        frequency >= range.first && frequency <= range.second

    fun channelOfFrequency(frequency: Int): WifiChannel? {
        var result: Pair<WifiChannel, WifiChannel>? = null
        if (inRange(frequency)) {
            channels.find { channel(frequency, it) != null }
        }
        return channel(frequency, result)
    }

    fun channelOfChannel(channel: Int): WifiChannel? {
        val result = channels.find {
            channel >= it.first.channel && channel <= it.second.channel
        }
        return if (result == null) null
        else WifiChannel(
            channel,
            result.first.frequency + (channel - result.first.channel) * WifiChannel.FREQUENCY_SPREAD
        )
    }

    fun getChannels(): List<WifiChannel> {
        val result = arrayListOf<WifiChannel>()
        channels.forEach {
            for (channel in it.first.channel..it.second.channel) {
                channelOfChannel(channel)?.let { result.add(it) }
            }
        }
        return result
    }

    internal fun channel(frequency: Int, channel: Pair<WifiChannel, WifiChannel>?): WifiChannel? {
        if (channel == null) return null
        val first = channel.first
        val last = channel.second
        val channel: Int =
            (((frequency - first.frequency).toDouble() / WifiChannel.FREQUENCY_SPREAD) + first.channel + .5).roundToInt()

        return if (channel >= first.channel && channel <= last.channel) WifiChannel(
            channel,
            frequency
        ) else null
    }
}