package com.dreampany.tools.data.enums.wifi

import android.net.wifi.ScanResult
import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.dreampany.framework.data.enums.BaseType
import com.dreampany.framework.misc.exts.isMinM
import com.dreampany.network.misc.channelWidth
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 16/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class Width(val frequency: Int) : BaseType {
    MHZ_20(20),
    MHZ_40(40),
    MHZ_80(80),
    MHZ_160(160),
    MHZ_80_PLUS(80); // should be two 80 and 80 - feature support

    override val value: String get() = name

    val frequencyHalf: Int get() = frequency / 2

    companion object {
        fun find(input: ScanResult): Width {
            try {
                val channelWidth = channelWidth(input)
                return Width.values()[channelWidth]
            } catch (error: Throwable) {
                return MHZ_20
            }
        }
    }
}