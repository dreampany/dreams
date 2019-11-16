package com.dreampany.tools.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.util.TimeUtilKt
import com.dreampany.tools.misc.Constants
import com.google.common.base.Objects
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-09-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@IgnoreExtraProperties
@Entity(
    indices = [Index(
        value = [Constants.Keys.Contact.ID],
        unique = true
    )],
    primaryKeys = [Constants.Keys.Contact.ID]
)
data class Contact(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    var name: String? = Constants.Default.NULL,
    @ColumnInfo(name = Constants.Keys.Contact.NICK_NAME)
    var nickName: String? = Constants.Default.NULL,
    @ColumnInfo(name = Constants.Keys.Contact.AVATAR_URL)
    var avatarUrl: String? = Constants.Default.NULL,
    @ColumnInfo(name = Constants.Keys.Contact.PHONE_NUMBER)
    var phoneNumber: String? = Constants.Default.NULL,
    var email: String? = Constants.Default.NULL,
    var address: String? = Constants.Default.NULL,
    @ColumnInfo(name = Constants.Keys.Contact.COUNTRY_CODE)
    var countryCode: String? = Constants.Default.NULL
) : Base() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {

    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {

    }

    constructor(
        id: String,
        name: String?,
        phoneNumber: String?
    ) : this(
        time = TimeUtilKt.currentMillis(),
        id = id,
        name = name,
        phoneNumber = phoneNumber
    ) {

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Contact
        return Objects.equal(this.id, item.id)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }
}