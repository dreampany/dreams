package com.dreampany.tools.data.enums.wifi

import androidx.annotation.StringRes
import com.dreampany.framework.data.enums.BaseType
import com.dreampany.tools.R
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 25/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class Band(@StringRes val resId : Int, val band : BandGHZ) : BaseType {
    GHZ2(R.string.title_wifi_band_2ghz, BandGHZ2()),
    GHZ5(R.string.title_wifi_band_2ghz, BandGHZ5());

    override val value: String get() = name
}
