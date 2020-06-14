package com.dreampany.tools.data.enums.news

import com.dreampany.framework.data.enums.BaseAction
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 26/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class NewsAction : BaseAction {
    DEFAULT, VIEW;

    override val value: String get() = name
}