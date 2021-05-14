package com.dreampany.hi.data.model

import androidx.room.Ignore
import com.dreampany.hi.currentMillis
import com.dreampany.hi.misc.Constant
import com.google.common.base.Objects
import kotlinx.parcelize.Parcelize


/**
 * Created by roman on 5/14/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Parcelize
data class Auth(
    override var time: Long = Constant.Default.LONG,
    override var id: String = Constant.Default.STRING
) : Base() {

    @Ignore
    constructor() : this(time = currentMillis) {
    }

    constructor(id: String) : this(time = currentMillis, id = id) {}

    override fun hashCode(): Int = Objects.hashCode(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Auth
        return Objects.equal(this.id, item.id)
    }
}