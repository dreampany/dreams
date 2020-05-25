package com.dreampany.tools.data.enums.wifi

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.dreampany.framework.data.enums.BaseType
import com.dreampany.tools.R
import com.dreampany.tools.misc.utils.WifiUtils
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 25/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class Strength(
    @DrawableRes val imageRes: Int,
    @ColorRes val colorRes: Int
) : BaseType {
    ZERO(R.drawable.ic_signal_wifi_0_bar, R.color.error),
    ONE(R.drawable.ic_signal_wifi_1_bar, R.color.warning),
    TWO(R.drawable.ic_signal_wifi_2_bar, R.color.warning),
    THREE(R.drawable.ic_signal_wifi_3_bar, R.color.success),
    FOUR(R.drawable.ic_signal_wifi_4_bar, R.color.success);

    override val value: String get() = name

    val isWeek: Boolean get() = ZERO == this

    companion object {
        fun calculate(level: Int): Strength {
            val values = values()
            val index = WifiUtils.calculateSignalLevel(level, values.size)
            return values.get(index)
        }

        fun reverse(strength: Strength): Strength {
            val values = values()
            val index = values.size - strength.ordinal.dec()
            return values.get(index)
        }
    }
}