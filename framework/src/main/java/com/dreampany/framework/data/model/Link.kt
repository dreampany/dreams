package com.dreampany.framework.data.model

import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 10/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class Link(
    val url: String,
    val title: String
) : BaseParcel()