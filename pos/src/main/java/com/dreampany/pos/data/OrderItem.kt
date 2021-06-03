package com.dreampany.pos.data

import com.google.common.base.Objects
import java.math.BigDecimal

/**
 * Created by roman on 5/20/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
data class OrderItem(
    var order: Order? = null,
    var menuItemId: Long = 0L,
    var name: String? = null,
    var quantity: Int = 0,
    var price: BigDecimal = BigDecimal.ZERO,
    var comment: String? = null,
    var orderItemModifiers : List<OrderItemModifier>? = null
) {

    override fun hashCode(): Int = Objects.hashCode(order, menuItemId)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as OrderItem
        return Objects.equal(this.order, item.order) &&
                Objects.equal(this.menuItemId, item.menuItemId)
    }

    override fun toString(): String = "OrderItem.name: $name"
}