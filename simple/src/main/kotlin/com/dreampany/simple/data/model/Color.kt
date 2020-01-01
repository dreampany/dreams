package com.dreampany.simple.data.model

import com.dreampany.simple.misc.Constants
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2020-01-01
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class Color(
    var primaryId: Int = Constants.Default.INT,
    var primaryDarkId: Int = Constants.Default.INT,
    var accentId: Int = Constants.Default.INT
) : BaseParcel() {

/*    constructor(primaryId: Int, primaryDarkId: Int) : this(primaryId = primaryId, primaryDarkId = primaryDarkId) {

    }*/
}