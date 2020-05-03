package com.dreampany.framework.data.enums

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class Action : Parcelable {
    DEFAULT, ADD, EDIT, UPDATE,
    DELETE, GET, LOAD, SEARCH,
    SYNC, OPEN, PLAY, DETAILS, VIEW, PREVIEW,
    OPTIONS, TRASH, FAVORITE, ARCHIVE,
    SOLVE, NEXT, CLICK, LONG_CLICK, SELECTED,
    PAGINATE, BLOCK, UNBLOCK, LOCK
}