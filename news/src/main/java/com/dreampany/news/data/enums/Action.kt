package com.dreampany.news.data.enums

import com.dreampany.framework.data.enums.BaseAction
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 16/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class Action : BaseAction {
    DEFAULT, BACK;
    override val value: String get() = name
}