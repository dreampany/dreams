package com.dreampany.history.data.enums

import android.os.Parcelable
import com.dreampany.frame.util.TextUtil
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-07-24
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class HistoryType: Parcelable {
    EVENT, BIRTH, DEATH;

    fun toTitle() : String {
        return TextUtil.toTitleCase(name)
    }
}