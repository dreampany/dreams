package com.dreampany.pos.data

import com.google.common.base.Objects
import java.math.BigDecimal

/**
 * Created by roman on 5/20/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
data class OrderItemModifier(
    var orderItem: OrderItem? = null,
    var modifierItemId: Long = 0L,
    var name: String? = null,
    var price: BigDecimal = 0.0.toBigDecimal()
) {

    override fun hashCode(): Int = Objects.hashCode(orderItem, modifierItemId)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as OrderItemModifier
        return Objects.equal(this.orderItem, item.orderItem) &&
                Objects.equal(this.modifierItemId, item.modifierItemId)
    }

    override fun toString(): String = "OrderItemModifier.name: $name"
}