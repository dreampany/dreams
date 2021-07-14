package com.dreampany.hi.data.enums

import com.dreampany.common.data.enums.BaseEnum
import com.dreampany.common.misc.exts.title
import kotlinx.parcelize.Parcelize

/**
 * Created by roman on 7/13/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class State : BaseEnum {
    DEFAULT, FAVORITE;

    override val value: String get() = name.title
}