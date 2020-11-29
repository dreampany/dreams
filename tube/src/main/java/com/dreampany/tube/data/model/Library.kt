package com.dreampany.tube.data.model

import android.os.Parcelable
import com.dreampany.framework.data.model.BaseParcel
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 11/29/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class Library(
    val type: Type
) : BaseParcel() {

    @Parcelize
    enum class Type(val value: String) : Parcelable {
        RECENT("recent"),
        FAVORITE("favorite");
    }
}