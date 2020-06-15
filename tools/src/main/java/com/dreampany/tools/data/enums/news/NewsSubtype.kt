package com.dreampany.tools.data.enums.news

import com.dreampany.framework.data.enums.BaseSubtype
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class NewsSubtype : BaseSubtype {
    DEFAULT, COUNTRY, GENERAL, HEALTH, BUSINESS, ENTERTAINMENT, SPORTS, SCIENCE, TECHNOLOGY;

    override val value: String get() = name
}