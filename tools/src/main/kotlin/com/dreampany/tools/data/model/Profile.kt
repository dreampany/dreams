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
data class Profile(
    var name: String? = Constants.Default.NULL,
    val designation: String? = Constants.Default.NULL,
    val phone: String? = Constants.Default.NULL,
    val email: String? = Constants.Default.NULL,
    val currentAddress: String? = Constants.Default.NULL,
    val permanentAddress: String? = Constants.Default.NULL
) : BaseParcel() {
}