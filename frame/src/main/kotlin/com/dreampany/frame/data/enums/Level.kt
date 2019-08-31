package com.dreampany.frame.data.enums

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-08-05
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class Level(val code: Int) : Parcelable {
    DEFAULT(0),
    A1(1), A2(2), A3(3), A4(4), A5(5), A6(6), A7(7), A8(8), A9(9), A10(10)
}