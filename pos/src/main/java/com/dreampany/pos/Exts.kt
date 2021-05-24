package com.dreampany.pos

import com.dreampany.pos.data.Order
import com.starmicronics.starioextension.ICommandBuilder
import com.starmicronics.starioextension.StarIoExt
import java.nio.charset.Charset

/**
 * Created by roman on 5/24/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */

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

        builder.appendCutPaper(ICommandBuilder.CutPaperAction.PartialCutWithFeed)
        builder.endDocument()
        return builder.commands
    }

private fun ICommandBuilder.addButlerTitle() {
    val encoding = Charset.forName("US-ASCII")
    appendAlignment(ICommandBuilder.AlignmentPosition.Center)
    appendFontStyle(ICommandBuilder.FontStyleType.A)
    appendEmphasis(true)
    append("BUTLER".toByteArray(encoding))
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