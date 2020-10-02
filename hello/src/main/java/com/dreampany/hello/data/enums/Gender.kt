package com.dreampany.hello.data.enums

import android.os.Parcelable
import com.dreampany.hello.misc.Constants
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class Gender(
    val value: String
) : Parcelable {
    MALE(Constants.Gender.MALE),
    FEMALE(Constants.Gender.FEMALE),
    OTHER(Constants.Gender.OTHER)
}