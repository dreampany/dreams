package com.dreampany.hello.data.model

import androidx.room.Entity
import androidx.room.Index
import com.dreampany.framework.data.model.BaseParcel
import com.dreampany.framework.misc.constant.Constant
import com.google.common.base.Objects
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 25/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@IgnoreExtraProperties
@Entity(
    indices = [Index(
        value = [Constant.Keys.ID],
        unique = true
    )],
    primaryKeys = [Constant.Keys.ID]
)
data class Auth(
    var id: String = Constant.Default.STRING,
    var email: String? = Constant.Default.NULL
) : BaseParcel() {

    constructor(id: String) : this(id = id, email = null) {

    }

    override fun hashCode(): Int = Objects.hashCode(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Auth
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String = "Auth.id:email: $id:$email"
}