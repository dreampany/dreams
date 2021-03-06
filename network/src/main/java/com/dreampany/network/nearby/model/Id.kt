package com.dreampany.network.nearby.model

import com.google.common.base.Objects


/**
 * Created by roman on 7/2/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
data class Id(val id: String, val source: String, val target: String) {

    override fun hashCode(): Int = Objects.hashCode(id, source, target)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Id
        return Objects.equal(this.id, item.id)
                && Objects.equal(this.source, item.source)
                && Objects.equal(this.target, item.target)
    }

    override fun toString(): String = "Id ($id) == $id"
}