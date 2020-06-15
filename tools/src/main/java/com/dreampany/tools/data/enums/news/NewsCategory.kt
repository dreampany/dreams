package com.dreampany.tools.data.enums.news

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 26/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class NewsCategory(val value: String) : Parcelable {
    GENERAL("general"),
    BUSINESS("business"),
    ENTERTAINMENT("entertainment"),
    HEALTH("health"),
    SCIENCE("science"),
    SPORTS("sports"),
    TECHNOLOGY("technology");
}