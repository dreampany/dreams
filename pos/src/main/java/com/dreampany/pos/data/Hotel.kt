package com.dreampany.pos.data

import com.google.common.base.Objects

/**
 * Created by roman on 5/20/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
data class Hotel(
    var name: String? = null,
    var location: Location? = null
) {

    override fun hashCode(): Int = Objects.hashCode(name, location)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Hotel
        return Objects.equal(this.name, item.name) &&
                Objects.equal(this.location, item.location)
    }

    override fun toString(): String = "Hotel.name: $name"
}