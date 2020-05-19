package com.dreampany.wifi.data.model.band

import android.os.Parcelable
import androidx.annotation.StringRes
import com.dreampany.wifi.R
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 14/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class WifiBand(
    @StringRes val resId: Int,
    val channels: WifiChannels
) : Parcelable {
    GHZ2(R.string.wifi_band_2ghz, WifiChannels2GHZ()),
    GHZ5(R.string.wifi_band_5ghz, WifiChannels5GHZ());

    val is5GHZ: Boolean get() = GHZ5 == this
    val toggle: WifiBand get() = if (is5GHZ) GHZ2 else GHZ5
}