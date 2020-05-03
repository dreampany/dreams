package com.dreampany.tools.data.enums.note

import com.dreampany.framework.data.enums.BaseSubtype
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class NoteSubtype : BaseSubtype {
    DEFAULT, INFO, MARKET;

    override val value: String get() = name
}