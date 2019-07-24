package com.dreampany.history.data.model

import com.dreampany.frame.data.model.BaseKt
import kotlinx.android.parcel.Parcelize

/**
 * Created by Roman-372 on 7/24/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class Demo (
    override var time: Long,
    override var id: String
) : BaseKt() {

}