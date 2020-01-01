package com.dreampany.framework.data.enums

import   android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-08-05
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class Level(val code: Int) : Parcelable {
    DEFAULT(code = 0),
    A1(code = 1), A2(code = 2), A3(code = 3), A4(code = 4), A5(code = 5),
    A6(code = 6), A7(code = 7), A8(code = 8), A9(code = 9), A10(code = 10)
}