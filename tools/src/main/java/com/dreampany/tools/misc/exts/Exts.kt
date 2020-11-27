package com.dreampany.tools.misc.exts

import android.widget.TextView
import androidx.annotation.ColorRes
import com.dreampany.framework.misc.exts.color
import com.dreampany.tools.R
import com.klinker.android.link_builder.Link
import com.klinker.android.link_builder.LinkBuilder
import java.util.*

/**
 * Created by roman on 11/25/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
val Boolean.favoriteIcon: Int get() = if (this) R.drawable.ic_favorite_red_24dp else R.drawable.ic_favorite_black_24dp

fun TextView.setSpan(
    items: List<String>,
    bold: String? = null,
    clickListener: Link.OnClickListener? = null,
    longClickListener: Link.OnLongClickListener? = null
): Boolean {
    val links: MutableList<Link> = ArrayList()
    var link: Link
    for (item in items) {
        link = Link(item)
            .setUnderlined(false)
            .setTextColor(context.color(R.color.material_grey700))
            .setTextColorOfHighlightedLink(context.color(R.color.colorAccent))
        if (clickListener != null) {
            link.setOnClickListener(clickListener)
        }
        if (longClickListener != null) {
            link.setOnLongClickListener(longClickListener)
        }
        links.add(link)
    }
    if (bold.isNullOrEmpty().not()) {
        link = Link(bold!!)
            .setUnderlined(false)
            .setBold(true)
            .setTextColor(context.color(R.color.material_grey700))
            .setTextColorOfHighlightedLink(context.color(R.color.colorAccent))
        links.add(link)
    }
    LinkBuilder.on(this)
        .addLinks(links)
        .build()
    return false
}

fun TextView.setSpan(
    items: List<String>,
    @ColorRes textColor: Int,
    @ColorRes textColorOfLink: Int,
    clickListener: Link.OnClickListener? = null,
    longClickListener: Link.OnLongClickListener? = null
): Boolean {
    val links: MutableList<Link> = ArrayList()
    var link: Link
    for (item in items) {
        link = Link(item)
            .setUnderlined(false)
            .setTextColor(context.color(textColor))
            .setTextColorOfHighlightedLink(context.color(textColorOfLink))
        if (clickListener != null) {
            link.setOnClickListener(clickListener)
        }
        if (longClickListener != null) {
            link.setOnLongClickListener(longClickListener)
        }
        links.add(link)
    }
    LinkBuilder.on(this)
        .addLinks(links)
        .build()
    return false
}