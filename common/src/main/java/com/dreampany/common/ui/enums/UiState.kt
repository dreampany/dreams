package com.dreampany.common.ui.enums

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class UiState : Parcelable {
    DEFAULT,
    SHOW_PROGRESS,
    HIDE_PROGRESS,
    EMPTY,
    ERROR,
    OFFLINE,
    ONLINE,
    CONTENT,
    EXTRA
}