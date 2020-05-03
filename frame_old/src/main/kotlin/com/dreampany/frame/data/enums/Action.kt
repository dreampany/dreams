package com.dreampany.frame.data.enums

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2020-01-01
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class Action : Parcelable {
    DEFAULT, ADD, EDIT, UPDATE,
    DELETE, GET
}