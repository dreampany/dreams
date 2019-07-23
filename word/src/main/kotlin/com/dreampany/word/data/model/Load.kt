package com.dreampany.word.data.model

import com.dreampany.frame.data.model.BaseKt
import com.dreampany.frame.util.TimeUtil
import com.dreampany.translation.data.model.TextTranslation
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by Roman-372 on 7/17/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
 data class Load(
    override var time: Long,
    override var id: String,
    var current: Int,
    var total: Int
) : BaseKt() {

    constructor(current: Int, total: Int) : this(TimeUtil.currentTime(), "", current, total) {

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as TextTranslation
        return Objects.equal(item.id, id)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }
}