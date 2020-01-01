package com.dreampany.simple.data.enums

import   android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2020-01-01
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class Quality(val code: Int) : Parcelable {
    DEFAULT(code = 0),
    LOW(code = 1),
    MEDIUM(code = 2),
    HIGH(code = 3)
}