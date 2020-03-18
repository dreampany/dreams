package com.dreampany.pair.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.common.data.model.Base
import com.dreampany.common.misc.constant.Constants
import com.dreampany.common.misc.util.Util
import com.google.common.base.Objects
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@IgnoreExtraProperties
@Entity(
    indices = [Index(
        value = [Constants.Keys.ID],
        unique = true
    )],
    primaryKeys = [Constants.Keys.ID]
)
data class User(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    var email: String? = Constants.Default.NULL,
    var password: String? = Constants.Default.NULL,
    var name: String? = Constants.Default.NULL,
    @Embedded
    var photo: Photo? = Constants.Default.NULL,
    var gender: String? = Constants.Default.NULL,
    var height: Int = Constants.Default.INT,
    var birthdate: Long = Constants.Default.LONG,
    var bio: String? = Constants.Default.NULL,
    var interest: String? = Constants.Default.NULL,
    var photos: List<Photo>? = Constants.Default.NULL,
    var latitude: Double = Constants.Default.DOUBLE,
    var longitude: Double = Constants.Default.DOUBLE,
    var token: String? = Constants.Default.NULL,
    var status: Boolean = Constants.Default.BOOLEAN
) : Base() {

    @Ignore
    constructor() : this(time = Util.currentMillis()) {

    }

    constructor(id: String) : this(time = Util.currentMillis(), id = id) {

    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as User
        return Objects.equal(item.id, id)
    }
}