package com.dreampany.pos.data

import com.google.common.base.Objects
import java.math.BigDecimal
import java.time.ZonedDateTime

/**
 * Created by roman on 5/20/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
data class Order(
    var orderNumber: Int = 0,
    var hotel: Hotel? = null,
    var roomNo: String? = null,
    var clientName: String? = null,
    var paymentType: String? = null,
    var clientPhone: String? = null,
    var scheduledDeliveryTime: ZonedDateTime? = null,
    var comment: String? = null,
    var cutlery: Long? = null,
    var items: List<OrderItem>? = null,
    var customItems: List<OrderItemCustom>? = null,
    var receiptAmount: BigDecimal = BigDecimal.ZERO,
    var totalNet: BigDecimal = BigDecimal.ZERO,
    var taxAmount: BigDecimal = BigDecimal.ZERO,
    var totalGross: BigDecimal = BigDecimal.ZERO
) {

    override fun hashCode(): Int = Objects.hashCode(orderNumber)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Order
        return Objects.equal(this.orderNumber, item.orderNumber)
    }

    override fun toString(): String = "Order.orderNumber: $orderNumber"
}