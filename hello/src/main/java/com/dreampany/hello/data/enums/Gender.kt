package com.dreampany.hello.data.enums

import com.dreampany.framework.data.enums.BaseType
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class Gender : BaseType {
    MALE,
    FEMALE,
    OTHER;

    override val value: String get() = name
}