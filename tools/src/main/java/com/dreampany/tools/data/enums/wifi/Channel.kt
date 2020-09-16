package com.dreampany.tools.data.enums.wifi

import com.google.common.base.Objects
import org.apache.commons.lang3.builder.CompareToBuilder
import org.apache.commons.lang3.builder.ToStringBuilder

/**
 * Created by roman on 16/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Channel(val channel : Int = 0, val frequency : Int = 0) : Comparable<Channel> {

    override fun hashCode(): Int = Objects.hashCode(channel, frequency)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Channel
        return Objects.equal(this.channel, item.channel)
                && Objects.equal(this.frequency, item.frequency)
    }

    override fun compareTo(other: Channel): Int {
        return CompareToBuilder()
            .append(channel, other.channel)
            .append(frequency, other.frequency)
            .toComparison()
    }

    override fun toString(): String = ToStringBuilder.reflectionToString(this)
}