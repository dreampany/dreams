package com.dreampany.framework.data.model

import com.google.common.base.Objects

/**
 * Created by Roman-372 on 7/4/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class Base : BaseParcel() {

    abstract var time: Long
    abstract var id: String

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Base
        return Objects.equal(item.id, id)
    }
}