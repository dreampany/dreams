package com.dreampany.tools.data.enums

import android.os.Parcelable
import com.dreampany.tools.misc.Constants
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-11-14
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class CoinSort(
    val value: String
) : Parcelable {
    MARKET_CAP(Constants.Keys.Quote.MARKET_CAP)
}