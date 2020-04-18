package com.dreampany.tools.data.enums.home

import com.dreampany.common.data.enums.BaseState
import com.dreampany.common.misc.extension.toTitle
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 16/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class State : BaseState {
    DEFAULT;
    override val title: String
        get() = name.toTitle()
}