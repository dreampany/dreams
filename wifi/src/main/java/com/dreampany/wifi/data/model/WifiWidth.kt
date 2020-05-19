package com.dreampany.wifi.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 14/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class WifiWidth(val frequencyWidth: Int) : Parcelable {
    MHZ_20(20),
    MHZ_40(40),
    MHZ_80(80),
    MHZ_160(160),
    MHZ_80_PLUS(80); // should be two 80 and 80 - feature support

    val frequencyWidthHalf: Int get() = frequencyWidth / 2
}