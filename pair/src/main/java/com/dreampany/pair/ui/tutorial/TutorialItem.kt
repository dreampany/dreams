package com.dreampany.pair.ui.tutorial

import androidx.annotation.DrawableRes
import com.google.common.base.Objects

/**
 * Created by roman on 3/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class TutorialItem(
    @DrawableRes val iconResId: Int,
    val title: String,
    val subtitle: String
) {
    override fun hashCode(): Int {
        return Objects.hashCode(iconResId, title)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as TutorialItem
        return Objects.equal(item.iconResId, iconResId)
                && Objects.equal(item.title, title)
    }
}