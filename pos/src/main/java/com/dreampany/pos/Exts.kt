package com.dreampany.pos

import com.dreampany.pos.data.Order
import com.dreampany.pos.data.OrderItem
import com.dreampany.pos.data.OrderItemCustom
import com.dreampany.pos.data.OrderItemModifier
import com.starmicronics.starioextension.ICommandBuilder
import com.starmicronics.starioextension.ICommandBuilder.AlignmentPosition.*
import com.starmicronics.starioextension.StarIoExt
import java.math.BigDecimal
import java.math.RoundingMode
import java.nio.charset.Charset
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


/**
 * Created by roman on 5/24/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */

private const val SINGLE_WIDTH_CHARS_PER_LINE: Int = 42
private var CHARS_PER_LINE = SINGLE_WIDTH_CHARS_PER_LINE
private const val DATE_TIME_FORMAT: String = "MMM dd, yyyy h:mm a"
private const val DATE_FORMAT: String = "MMM dd, yyyy"
private const val TIME_FORMAT: String = "h:mm a"
private const val CURRENCY_FORMAT = "$%s"

val Order.receipt: ByteArray
    get() {
        val builder = StarIoExt.createCommandBuilder(StarIoExt.Emulation.StarDotImpact)
        builder.beginDocument()

        builder.appendCodePage(ICommandBuilder.CodePageType.CP998)
        builder.appendInternational(ICommandBuilder.InternationalType.USA)
        builder.appendCharacterSpace(0)

        // add receipt data
        builder.addButlerTitle()
        builder.addHotelLocationAndOrderNumber(this)
        builder.addCurrentDateTime(this)
        builder.addDeliveryLabel()
        builder.addClientInfo(this)
        builder.addOrderDetailsLabel()
        builder.addComment(this)
        builder.addCutlery(this)
        builder.addItems(this)
        builder.addCustomItems(this)
        builder.addPrices(this)
        builder.addSignatureAndTipLabel()
        builder.addMarketingMessage()

        builder.appendCutPaper(ICommandBuilder.CutPaperAction.PartialCutWithFeed)
        builder.endDocument()
        return builder.commands
    }

private fun ICommandBuilder.addButlerTitle() {
    val encoding = Charset.forName("US-ASCII")
    appendAlignment(Center)
    appendEmphasis("BUTLER".toByteArray(encoding))
    appendLineFeed(3)
}

private fun ICommandBuilder.addHotelLocationAndOrderNumber(order: Order) {
    val encoding = Charset.forName("US-ASCII")
    appendEmphasis(false)
    appendAlignment(
        "${order.hotel?.location?.name} - Order #${order.orderNumber}".toByteArray(
            encoding
        ), Center
    )
    appendLineFeed(3)
}

private fun ICommandBuilder.addCurrentDateTime(order: Order) {
    var dateTime = ZonedDateTime.now()
    try {
        val timeZone = order.hotel?.location?.city?.timeZone
        dateTime = dateTime.withZoneSameInstant(ZoneId.of(timeZone))
    } catch (error: Throwable) {

    }

    val encoding = Charset.forName("US-ASCII")
    appendAlignment(dateTime.formatDateTime.toByteArray(encoding), Left)
    appendLineFeed()
}

private fun ICommandBuilder.addDeliveryLabel() {
    val encoding = Charset.forName("US-ASCII")
    appendAlignment("------------------------------------------".toByteArray(encoding), Center)
    appendLineFeed()
    appendAlignment("DELIVERY INFORMATION".toByteArray(encoding), Center)
    appendLineFeed()
    appendAlignment("------------------------------------------".toByteArray(encoding), Center)
    appendLineFeed()
}

private fun ICommandBuilder.addClientInfo(order: Order) {
    val encoding = Charset.forName("US-ASCII")
    appendText(order.clientName.value, "Payment type:")
    appendLineFeed()

    var payment = order.paymentType
    payment = payment?.replace("_", " ") ?: ""

    appendText(order.clientPhone.value, payment)
    appendLineFeed(2)

    order.scheduledDeliveryTime?.let {
        var scheduledDeliveryTime = it
        try {
            val timeZone = order.hotel?.location?.city?.timeZone
            scheduledDeliveryTime = scheduledDeliveryTime.withZoneSameInstant(ZoneId.of(timeZone))
        } catch (error: Throwable) {

        }

        val startWindowTime = scheduledDeliveryTime.formatTime
        val endWindowTime = scheduledDeliveryTime.plusMinutes(15).formatTime

        appendEmphasis(true)
        appendAlignment(("Window: $startWindowTime - $endWindowTime").toByteArray(encoding), Center)
        appendEmphasis(false)
        appendLineFeed()
    }

    val hotelRoom = order.hotel?.name + ", Room: " + order.roomNo
    val address = order.hotel?.address
    val hotelAddress = address?.number + " " + address?.street + " " + address?.town
    appendText(hotelRoom)
    appendLineFeed()
    appendText(hotelAddress)
    appendLineFeed()
}

private fun ICommandBuilder.addOrderDetailsLabel() {
    val encoding = Charset.forName("US-ASCII")
    appendAlignment("------------------------------------------".toByteArray(encoding), Center)
    appendLineFeed()
    appendAlignment("ORDER DETAILS".toByteArray(encoding), Center)
    appendLineFeed()
    appendAlignment("------------------------------------------".toByteArray(encoding), Center)
    appendLineFeed()
}

private fun ICommandBuilder.addComment(order: Order) {
    order.comment?.let {
        val encoding = Charset.forName("US-ASCII")
        appendAlignment(Left)
        appendEmphasis("Comment: $it".toByteArray(encoding))
        appendLineFeed()
    }
}

private fun ICommandBuilder.addCutlery(order: Order) {
    order.cutlery?.let {
        val encoding = Charset.forName("US-ASCII")
        appendAlignment(Left)
        appendEmphasis("Cutlery: $it".toByteArray(encoding))
        appendLineFeed()
    }
}

private fun ICommandBuilder.addItems(order: Order) {
    order.items?.forEach {
        this.addItem(it)
    }
}

private fun ICommandBuilder.addCustomItems(order: Order) {
    order.customItems?.forEach {
        this.addItem(it)
    }
}

private fun ICommandBuilder.addItem(item: OrderItem) {
    val encoding = Charset.forName("US-ASCII")

    appendEmphasis(true)
    appendText("${item.quantity} x ${item.name}", item.price.multiply(BigDecimal(item.quantity)).formatUsd)
    appendEmphasis(false)
    appendLineFeed()

    item.orderItemModifiers?.forEach {
        this.addItemModifier(item, it)
    }

    item.comment?.let {
        appendAlignment("Comment: $it".toByteArray(encoding), Left)
        appendLineFeed()
    }
}

private fun ICommandBuilder.addItem(item: OrderItemCustom) {
    appendEmphasis(true)
    appendText("${item.quantity} x ${item.name}", item.price.multiply(BigDecimal(item.quantity)).formatUsd)
    appendEmphasis(false)
    appendLineFeed()
}

private fun ICommandBuilder.addItemModifier(item: OrderItem, modifier: OrderItemModifier) {
    appendText("${modifier.name}", modifier.price.multiply(BigDecimal(item.quantity)).formatUsd)
    appendLineFeed()
}

private fun ICommandBuilder.addPrices(order: Order) {
    val encoding = Charset.forName("US-ASCII")

    appendAlignment("------------------------------------------".toByteArray(encoding), Left)
    appendLineFeed()
    appendText("Subtotal",order.receiptAmount.formatUsd)
    appendLineFeed()

    val discount = order.receiptAmount.subtract(order.totalNet)
    if (BigDecimal.ZERO.compareTo(discount) != 0) {
        appendText("Discount", "-${discount.formatUsd}")
        appendLineFeed()
    }

    appendText("Sales Tax", order.taxAmount.formatUsd)
    appendLineFeed()

    appendAlignment("------------------------------------------".toByteArray(encoding), Left)
    appendLineFeed()

    appendEmphasis(true)
    appendText("Total", order.totalGross.formatUsd)
    appendEmphasis(false)
    appendLineFeed(2)
}

private fun ICommandBuilder.addSignatureAndTipLabel() {
    appendText("Sign", "Tip")
    appendLineFeed()

    appendAlignment(Left)
    appendText("___________________________", "\$_______")
    appendLineFeed(2)
}

private fun ICommandBuilder.addMarketingMessage() {
    val encoding = Charset.forName("US-ASCII")
    appendAlignment("------------------------------------------".toByteArray(encoding), Center)
    appendLineFeed()
    appendAlignment("Thank you and enjoy your meal".toByteArray(encoding), Center)
    appendLineFeed()
    appendAlignment("------------------------------------------".toByteArray(encoding), Center)
    appendLineFeed()
}

private fun ICommandBuilder.appendText(text: String) {
    val encoding = Charset.forName("US-ASCII")
    append(text.toByteArray(encoding))
}

private fun ICommandBuilder.appendText(leftText: String, rightText: String) {
    val spaces =
        String(CharArray(CHARS_PER_LINE - (leftText.length + rightText.length))).replace(
            '\u0000',
            ' '
        )
    this.appendText(leftText + spaces + rightText)
}

private val ZonedDateTime.formatDateTime: String
    get() = format(
        DateTimeFormatter.ofPattern(
            DATE_TIME_FORMAT
        )
    )

private val ZonedDateTime.formatDate: String get() = format(DateTimeFormatter.ofPattern(DATE_FORMAT))

private val ZonedDateTime.formatTime: String get() = format(DateTimeFormatter.ofPattern(TIME_FORMAT))

private val BigDecimal.formatUsd: String
    get() = String.format(
        CURRENCY_FORMAT,
        this.setScale(2, RoundingMode.HALF_UP).toPlainString()
    )

val String?.value : String get() = this ?: ""