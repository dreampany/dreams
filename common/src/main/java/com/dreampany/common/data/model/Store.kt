package com.dreampany.common.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.common.misc.constant.Constant
import com.dreampany.common.misc.exts.currentMillis
import com.google.common.base.Objects
import kotlinx.parcelize.Parcelize

/**
 * Created by roman on 7/13/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Parcelize
@Entity(
    indices = [Index(
        value = [Constant.Keys.ID, Constant.Keys.TYPE, Constant.Keys.SUBTYPE, Constant.Keys.STATE],
        unique = true
    )],
    primaryKeys = [Constant.Keys.ID, Constant.Keys.TYPE, Constant.Keys.SUBTYPE, Constant.Keys.STATE]
)
data class Store(
    override var time: Long = Constant.Default.LONG,
    override var id: String = Constant.Default.STRING,
    val type: String = Constant.Default.STRING,
    val subtype: String = Constant.Default.STRING,
    val state: String = Constant.Default.STRING,
    val extra: String? = Constant.Default.NULL
): Base() {

    @Ignore
    constructor() : this(time = currentMillis)
    constructor(id: String) : this(time = currentMillis, id = id)
    constructor(id: String, type: String, subtype: String, state: String) : this(
        time = currentMillis,
        id = id,
        type = type,
        subtype = subtype,
        state = state
    )

    override fun hashCode(): Int = Objects.hashCode(id, type, subtype, state)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Store
        return Objects.equal(item.id, id) &&
                Objects.equal(item.type, type) &&
                Objects.equal(item.subtype, subtype) &&
                Objects.equal(item.state, state)
    }

    override fun toString(): String = "Store.id: $id"

    fun hasProperty(type: String, subtype: String, state: String): Boolean {
        return (Objects.equal(type, this.type)
                && Objects.equal(subtype, this.subtype)
                && Objects.equal(state, this.state))
    }
}