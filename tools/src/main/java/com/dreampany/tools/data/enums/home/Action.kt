package com.dreampany.tools.data.enums.home

import com.dreampany.common.data.enums.BaseAction
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 16/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class Action : BaseAction {
    DEFAULT;
    override val value: String get() = name
}