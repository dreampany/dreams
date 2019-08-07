package com.dreampany.tools.data.enums

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-08-05
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class AddressType : Parcelable {
    DEFAULT,
    UNKNOWN,
    WORK,
    HOME
}