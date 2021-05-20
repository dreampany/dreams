package com.dreampany.pos.data

import com.google.common.base.Objects

/**
 * Created by roman on 5/20/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
data class City(
    var id: Long = 0L,
    var timeZone: String? = null
) {

    override fun hashCode(): Int = Objects.hashCode(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as City
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String = "City.id: $id"
}