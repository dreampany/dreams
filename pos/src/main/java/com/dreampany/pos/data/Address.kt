package com.dreampany.pos.data

import com.google.common.base.Objects

/**
 * Created by roman on 5/20/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
data class Address(
    var number: String? = null,
    var street: String? = null,
    var town: String? = null
) {

    override fun hashCode(): Int = Objects.hashCode(number, street, town)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Address
        return Objects.equal(this.number, item.number) &&
                Objects.equal(this.street, item.street) &&
                Objects.equal(this.town, item.town)
    }

    override fun toString(): String = "Address.number: $number"
}