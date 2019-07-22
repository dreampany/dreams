package com.dreampany.frame.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.frame.misc.Constants
import com.dreampany.frame.util.TimeUtil
import com.google.common.base.Objects
import com.google.firebase.firestore.IgnoreExtraProperties

/**
 * Created by Roman-372 on 7/19/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@IgnoreExtraProperties
@Entity(
    indices = [Index(
        value = [Constants.Key.ID, Constants.Key.TYPE, Constants.Key.SUBTYPE, Constants.Key.STATE],
        unique = true
    )],
    primaryKeys = [Constants.Key.ID, Constants.Key.TYPE, Constants.Key.SUBTYPE, Constants.Key.STATE]
)
class State(
    time: Long,
    id: String
) : BaseKt(time, id) {

    lateinit var type: String
    lateinit var subtype: String
    lateinit var state: String

    @Ignore
    constructor() : this(TimeUtil.currentTime(), "") {
    }

    constructor(id: String, type: String, subtype: String, state: String)
            : this(TimeUtil.currentTime(), id) {
        this.type = type
        this.subtype = subtype
        this.state = state
    }

    @Ignore
    private constructor(parcel: Parcel) : this(parcel.readLong(), parcel.readString()!!) {
        type = parcel.readString()!!
        subtype = parcel.readString()!!
        state = parcel.readString()!!
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(time)
        dest.writeString(id)
        dest.writeString(type)
        dest.writeString(subtype)
        dest.writeString(state)
    }

    companion object CREATOR : Parcelable.Creator<State> {
        override fun createFromParcel(parcel: Parcel): State {
            return State(parcel)
        }

        override fun newArray(size: Int): Array<State?> {
            return arrayOfNulls(size)
        }
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