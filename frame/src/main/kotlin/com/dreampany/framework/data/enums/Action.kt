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
enum class Action : Parcelable {
    DEFAULT, ADD, EDIT, UPDATE,
    DELETE, GET, LOAD, SEARCH,
    SYNC, OPEN, PLAY, DETAILS,
    OPTIONS, TRASH, FAVORITE, ARCHIVE,
    SOLVE
}