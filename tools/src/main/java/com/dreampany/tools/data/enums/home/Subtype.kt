package com.dreampany.tools.data.enums.home

import com.dreampany.framework.data.enums.BaseSubtype
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class Subtype : BaseSubtype {
    DEFAULT, CRYPTO, QUESTION, RADIO, SETTINGS, APPS, RATE_US, FEEDBACK, INVITE, LICENSE, ABOUT;

    override val value: String get() = name
}