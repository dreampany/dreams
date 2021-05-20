package com.dreampany.pos.data

import com.google.common.base.Objects

/**
 * Created by roman on 5/20/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
data class OrderItem(
    var name: String? = null,
    var quantity: Int = 0,
    var price: Double = 0.0,
    var comment: String? = null
) {

    override fun hashCode(): Int = Objects.hashCode(name)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as OrderItem
        return Objects.equal(this.name, item.name)
    }

    override fun toString(): String = "OrderItem.name: $name"
}