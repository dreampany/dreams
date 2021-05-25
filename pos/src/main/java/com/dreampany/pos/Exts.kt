package com.dreampany.pos

import com.dreampany.pos.data.Order
import com.starmicronics.starioextension.ICommandBuilder
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
    appendAlignment(ICommandBuilder.AlignmentPosition.Center)
    appendFontStyle(ICommandBuilder.FontStyleType.B)
    appendEmphasis(false)
    append("${order.hotel?.location?.name} - Order #${order.orderNumber}".toByteArray(encoding))
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
    appendAlignment(ICommandBuilder.AlignmentPosition.Left)
    appendFontStyle(ICommandBuilder.FontStyleType.B)
    append(dateTime.formatDateTime.toByteArray(encoding))
    appendLineFeed()
}

private fun ICommandBuilder.addDeliveryLabel() {
    val encoding = Charset.forName("US-ASCII")
    appendAlignment(ICommandBuilder.AlignmentPosition.Center)
    appendFontStyle(ICommandBuilder.FontStyleType.B)
    append("----------------------------------------".toByteArray(encoding))
    appendLineFeed()
    append("DELIVERY INFORMATION".toByteArray(encoding))
    appendLineFeed()
    append("----------------------------------------".toByteArray(encoding))
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