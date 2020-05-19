package com.dreampany.wifi.data.enums

import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.dreampany.wifi.R
import com.dreampany.wifi.misc.WifiUtils
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 14/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class WifiStrength(
    @DrawableRes val imageRes: Int,
    @ColorRes val colorRes: Int
) : Parcelable {
    ZERO(R.drawable.ic_signal_wifi_0_bar, R.color.error),
    ONE(R.drawable.ic_signal_wifi_1_bar, R.color.warning),
    TWO(R.drawable.ic_signal_wifi_2_bar, R.color.warning),
    THREE(R.drawable.ic_signal_wifi_3_bar, R.color.success),
    FOUR(R.drawable.ic_signal_wifi_4_bar, R.color.success);

    val week: Boolean get() = ZERO == this

    companion object {
        fun calculate(level: Int): WifiStrength {
            val values = values()
            val index = WifiUtils.calculateSignalLevel(level, values.size)
            return values.get(index)
        }

        fun reverse(strength: WifiStrength): WifiStrength {
            val values = values()
            val index = values.size - strength.ordinal.dec()
            return values.get(index)
        }
    }
}