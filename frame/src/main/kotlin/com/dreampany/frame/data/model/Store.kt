package com.dreampany.frame.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.frame.misc.Constants
import com.dreampany.frame.util.TimeUtil
import com.google.common.base.Objects
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-07-25
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@IgnoreExtraProperties
@Entity(
    indices = [Index(
        value = [Constants.Key.ID, Constants.Key.TYPE, Constants.Key.SUBTYPE, Constants.Key.STATE],
        unique = true
    )],
    primaryKeys = [Constants.Key.ID, Constants.Key.TYPE, Constants.Key.SUBTYPE, Constants.Key.STATE]
)
data class Store(
    override var time: Long,
    override var id: String,
    var type: String,
    var subtype: String,
    var state: String,
    var data: String?
) : BaseKt() {

    @Ignore
    constructor() : this("", "", "", "", "") {
    }

    constructor(id: String, type: String, subtype: String, state: String, data: String?)
            : this(TimeUtil.currentTime(), id, type, subtype, state, data) {
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as State
        return Objects.equal(item.id, id) &&
                Objects.equal(item.type, type) &&
                Objects.equal(item.subtype, subtype) &&
                Objects.equal(item.state, state)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id, type, subtype, state)
    }

    fun hasProperty(type: String, subtype: String, state: String): Boolean {
        return (Objects.equal(type, this.type)
                && Objects.equal(subtype, this.subtype)
                && Objects.equal(state, this.state))
    }
}