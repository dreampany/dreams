package com.dreampany.tools.data.model.crypto

import com.dreampany.framework.data.model.BaseParcel
import com.dreampany.framework.misc.constant.Constant
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 10/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class Market(
    val id: String,
    val name: String,
    val image: String?
) : BaseParcel() {
    companion object {
        val DEFAULT = Market(Constant.Default.STRING, Constant.Default.STRING, Constant.Default.NULL)
    }
}