package com.dreampany.framework.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.misc.Constants
import com.dreampany.framework.util.TimeUtilKt
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
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    var type: Type = Type.DEFAULT,
    var subtype: Subtype = Subtype.DEFAULT,
    var state: State = State.DEFAULT,
    var data: String? = Constants.Default.NULL
) : Base() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {

    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {

    }

    constructor(
        id: String,
        type: Type,
        subtype: Subtype,
        state: State
    ) : this(
        time = TimeUtilKt.currentMillis(),
        id = id,
        type = type,
        subtype = subtype,
        state = state
    ) {

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Store
        return Objects.equal(item.id, id) &&
            Objects.equal(item.type, type) &&
            Objects.equal(item.subtype, subtype) &&
            Objects.equal(item.state, state)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id, type, subtype, state)
    }

    fun hasProperty(type: Type, subtype: Subtype, state: State): Boolean {
        return (Objects.equal(type, this.type)
            && Objects.equal(subtype, this.subtype)
            && Objects.equal(state, this.state))
    }
}