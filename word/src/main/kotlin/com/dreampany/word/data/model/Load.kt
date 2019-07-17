package com.dreampany.word.data.model

import com.dreampany.frame.data.model.BaseKt
import kotlinx.android.parcel.Parcelize

/**
 * Created by Roman-372 on 7/17/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
class Load(
    override val id: String,
    override val time: Long
) : BaseKt() {

    constructor(current: Int, total: Int) : this("", 0L) {
        this.current = current
        this.total = total
    }

    @Transient
    var current: Int = 0
    @Transient
    var total: Int = 0
}