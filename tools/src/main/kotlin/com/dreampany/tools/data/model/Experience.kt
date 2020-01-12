package com.dreampany.tools.data.model

import com.dreampany.framework.data.model.BaseParcel
import com.dreampany.tools.misc.Constants
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2020-01-11
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class Experience(
    var company: String? = Constants.Default.NULL,
    var location: String? = Constants.Default.NULL,
    var designation: String? = Constants.Default.NULL,
    var description: String? = Constants.Default.NULL,
    var from: Long = 0L,
    var to: Long = 0L
) : BaseParcel() {
}