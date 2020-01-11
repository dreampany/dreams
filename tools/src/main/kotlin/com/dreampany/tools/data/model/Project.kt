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
data class Project(
    var title: String? = null,
    val subtitle: String? = null,
    val detail: String? = null,
    val description: String? = null,
    val from: Long = 0L,
    val to: Long = 0L
) : BaseParcel() {
}