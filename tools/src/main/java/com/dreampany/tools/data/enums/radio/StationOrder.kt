package com.dreampany.tools.data.enums.radio

import android.os.Parcelable
import com.dreampany.tools.misc.constant.RadioConstants
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 3/21/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class StationOrder(
    val value: String
) : Parcelable {
    NAME(RadioConstants.Keys.Station.Order.NAME),
    CLICK_COUNT(RadioConstants.Keys.Station.Order.CLICK_COUNT)
}