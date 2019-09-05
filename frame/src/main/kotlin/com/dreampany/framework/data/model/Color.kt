package com.dreampany.framework.data.model

import com.dreampany.framework.misc.Constants
import kotlinx.android.parcel.Parcelize

/**
 * Created by Roman-372 on 8/2/2019
 * Copyright (c) 2019 bjit. All rights reserved.
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