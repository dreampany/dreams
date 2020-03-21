package com.dreampany.tools.data.enums

import android.os.Parcelable
import com.dreampany.tools.misc.constant.AppConstants
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 3/21/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class CoinSort(
    val value: String
) : Parcelable {
    MARKET_CAP(AppConstants.Keys.Quote.MARKET_CAP)
}