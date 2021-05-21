package com.dreampany.pos.data

import com.google.common.base.Objects

/**
 * Created by roman on 5/20/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
data class OrderItemCustom(
    var order: Order? = null,
    var name: String? = null,
    var price: Double = 0.0,
    var quantity: Int = 0
) {

    override fun hashCode(): Int = Objects.hashCode(order, name, price)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as OrderItemCustom
        return Objects.equal(this.order, item.order) &&
                Objects.equal(this.name, item.name) &&
                Objects.equal(this.price, item.price)
    }

    override fun toString(): String = "OrderItemCustom.name: $name"
}