package com.dreampany.pos

import com.dreampany.pos.data.Order
import com.starmicronics.starioextension.ICommandBuilder
import com.starmicronics.starioextension.ICommandBuilder.AlignmentPosition.*
import com.starmicronics.starioextension.StarIoExt
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

private const val DATE_TIME_FORMAT: String = "MMM dd, yyyy h:mm a"
private const val DATE_FORMAT: String = "MMM dd, yyyy"
private const val TIME_FORMAT: String = "h:mm a"

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
        builder.addSignatureAndTipLabel()

        builder.appendCutPaper(ICommandBuilder.CutPaperAction.PartialCutWithFeed)
        builder.endDocument()
        return builder.commands
    }

private fun ICommandBuilder.addButlerTitle() {
    val encoding = Charset.forName("US-ASCII")
    appendAlignment(ICommandBuilder.AlignmentPosition.Center)
    appendFontStyle(ICommandBuilder.FontStyleType.A)
    appendEmphasis("BUTLER".toByteArray(encoding))
    appendLineFeed(3)
}

private fun ICommandBuilder.addHotelLocationAndOrderNumber(order: Order) {
    val encoding = Charset.forName("US-ASCII")
    appendFontStyle(ICommandBuilder.FontStyleType.B)
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
    appendAlignment("----------------------------------------".toByteArray(encoding), Center)
    appendLineFeed()
    appendAlignment("DELIVERY INFORMATION".toByteArray(encoding), Center)
    appendLineFeed()
    appendAlignment("----------------------------------------".toByteArray(encoding), Center)
    appendLineFeed()
}

private fun ICommandBuilder.addClientInfo(order: Order) {
    val encoding = Charset.forName("US-ASCII")
    appendAlignment(order.clientName?.toByteArray(encoding), Left)
    appendAlignment("Payment type:".toByteArray(encoding), Right)
    appendLineFeed()

    var payment = order.paymentType
    payment = payment?.replace("_", " ") ?: ""

    appendAlignment(order.clientPhone?.toByteArray(encoding), Left)
    appendAlignment(payment.toByteArray(encoding), Right)
    appendLineFeed()

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
    appendAlignment(hotelRoom.toByteArray(encoding), Left)
    appendLineFeed()
    appendAlignment(hotelAddress.toByteArray(encoding), Left)
    appendLineFeed()
}

private fun ICommandBuilder.addOrderDetailsLabel() {
    val encoding = Charset.forName("US-ASCII")
    appendAlignment(ICommandBuilder.AlignmentPosition.Center)
    append("----------------------------------------".toByteArray(encoding))
    appendLineFeed()
    append("ORDER DETAILS".toByteArray(encoding))
    appendLineFeed()
    append("----------------------------------------".toByteArray(encoding))
    appendLineFeed()
}

private fun ICommandBuilder.addComment(order: Order) {
    order.comment?.let {
        val encoding = Charset.forName("US-ASCII")
        appendAlignment(ICommandBuilder.AlignmentPosition.Left)
        appendEmphasis("Comment: $it".toByteArray(encoding))
        appendLineFeed()
    }
}

private fun ICommandBuilder.addSignatureAndTipLabel() {
    val encoding = Charset.forName("US-ASCII")

    appendAlignment(ICommandBuilder.AlignmentPosition.Left)
    append("Sign".toByteArray(encoding))
    appendAlignment(ICommandBuilder.AlignmentPosition.Right)
    append("Tip".toByteArray(encoding))
    appendLineFeed()

    appendAlignment(ICommandBuilder.AlignmentPosition.Left)
    append("______________________________".toByteArray(encoding))
    appendAlignment(ICommandBuilder.AlignmentPosition.Right)
    append("\$_______".toByteArray(encoding))
    appendLineFeed()
}

private val ZonedDateTime.formatDateTime: String
    get() = format(
        DateTimeFormatter.ofPattern(
            DATE_TIME_FORMAT
        )
    )

private val ZonedDateTime.formatDate: String get() = format(DateTimeFormatter.ofPattern(DATE_FORMAT))

private val ZonedDateTime.formatTime: String get() = format(DateTimeFormatter.ofPattern(TIME_FORMAT))