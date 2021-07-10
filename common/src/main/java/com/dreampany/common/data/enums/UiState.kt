package com.dreampany.common.data.enums

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by roman on 7/11/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
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