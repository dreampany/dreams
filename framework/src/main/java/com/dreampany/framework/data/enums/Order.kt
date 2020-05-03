package com.dreampany.framework.data.enums

import android.os.Parcelable
import com.dreampany.framework.misc.constant.Constants
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 3/21/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class Order(
    val value: String
) : Parcelable {
    ASCENDING(Constants.Values.Order.ASCENDING),
    DESCENDING(Constants.Values.Order.DESCENDING)
}