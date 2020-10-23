package com.dreampany.tube.data.model

import android.os.Parcelable
import com.dreampany.framework.data.model.BaseParcel
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 23/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class Event(
    val type: Type
) : BaseParcel() {

    @Parcelize
    enum class Type(val value: String) : Parcelable {
        LIVE("live"), UPCOMING("upcoming");
    }
}