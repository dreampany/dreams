package com.dreampany.framework.data.enums

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-08-06
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class State : Parcelable {
    DEFAULT, ERROR, SETTINGS, LICENSE, ABOUT, RAW, FULL,
    HOME, TRASH, FAVORITE, ARCHIVED, RECENT, HISTORY,
    WRONG, RIGHT, TRACK, ASSETS
}