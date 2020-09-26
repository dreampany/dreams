package com.dreampany.framework.data.enums

import android.os.Parcelable
import com.dreampany.framework.misc.constant.Constant
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
    ASCENDING(Constant.Values.Order.ASCENDING),
    DESCENDING(Constant.Values.Order.DESCENDING)
}