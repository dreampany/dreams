package com.dreampany.tube.data.enums

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 29/7/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class CategoryType(val value: String) : Parcelable {
    DEFAULT("default"), REGION("region"), LIVE("live"), NEWS("news");

    val isRegion : Boolean
        get() = this == REGION

    val isFixed : Boolean
        get() = this == REGION || this == LIVE
}