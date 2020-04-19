package com.dreampany.common.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.common.misc.constant.Constants
import com.dreampany.common.misc.util.Util
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 20/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@Entity(
    indices = [Index(
        value = [Constants.Keys.ID, Constants.Keys.TYPE, Constants.Keys.SUBTYPE, Constants.Keys.STATE],
        unique = true
    )],
    primaryKeys = [Constants.Keys.ID, Constants.Keys.TYPE, Constants.Keys.SUBTYPE, Constants.Keys.STATE]
)
data class Store(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    val type: String = Constants.Default.STRING,
    val subtype: String = Constants.Default.STRING,
    val state: String = Constants.Default.STRING,
    val extra: String? = Constants.Default.NULL
) : Base() {

    @Ignore
    constructor() : this(time = Util.currentMillis())
    constructor(id: String) : this(time = Util.currentMillis(), id = id)
    constructor(id: String, type: String, subtype: String, state: String) : this(
        time = Util.currentMillis(),
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

    fun hasProperty(type: String, subtype: String, state: String): Boolean {
        return (Objects.equal(type, this.type)
                && Objects.equal(subtype, this.subtype)
                && Objects.equal(state, this.state))
    }
}