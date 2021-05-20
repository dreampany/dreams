package com.dreampany.pos.data

import com.google.common.base.Objects

/**
 * Created by roman on 5/20/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
data class Location(
    var name: String? = null,
    var city: City? = null
) {

    override fun hashCode(): Int = Objects.hashCode(city)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Location
        return Objects.equal(this.name, item.name)
    }

    override fun toString(): String = "Location.name: $name"
}