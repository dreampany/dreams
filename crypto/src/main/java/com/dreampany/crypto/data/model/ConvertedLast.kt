package com.dreampany.crypto.data.model

import com.dreampany.framework.data.model.BaseParcel
import com.dreampany.framework.misc.constant.Constant
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 11/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class ConvertedLast(
    val btc: Double,
    val eth: Double,
    val usd: Double
) : BaseParcel() {
    companion object {
        val DEFAULT = ConvertedLast(
            Constant.Default.DOUBLE,
            Constant.Default.DOUBLE,
            Constant.Default.DOUBLE
        )
    }
}