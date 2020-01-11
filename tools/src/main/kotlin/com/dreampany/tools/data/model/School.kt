package com.dreampany.tools.data.model

import com.dreampany.framework.data.model.BaseParcel
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2020-01-11
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class School(
    var name: String? = null,
    val location: String? = null,
    val degree: String? = null,
    val description: String? = null
) : BaseParcel() {
}