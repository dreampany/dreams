package com.dreampany.tools.ui.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.dreampany.common.data.model.BaseParcel
import com.dreampany.tools.data.enums.Subtype
import com.dreampany.tools.data.enums.Type
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class FeatureItem(
    val type: Type,
    val subtype: Subtype,
    @DrawableRes
    val iconRes: Int,
    @StringRes
    val titleRes: Int,
    val color: Int
) : BaseParcel() {

    override fun hashCode(): Int {
        return Objects.hashCode(type, subtype)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as FeatureItem
        return Objects.equal(item.type, type) && Objects.equal(item.subtype, subtype)
    }
}