package com.dreampany.tools.data.model

import com.dreampany.frame.data.model.Base
import com.dreampany.tools.misc.Constants
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-09-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class QuizOption(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING
) : Base() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as QuizOption
        return Objects.equal(item.id, id)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }
}