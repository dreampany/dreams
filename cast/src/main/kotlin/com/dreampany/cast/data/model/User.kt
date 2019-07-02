package com.dreampany.cast.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.cast.misc.Constants
import com.dreampany.frame.data.model.Base
import com.google.firebase.database.IgnoreExtraProperties

/**
 * Created by Roman-372 on 6/26/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@IgnoreExtraProperties
@Entity(
    indices = [Index(value = [Constants.User.ID], unique = true)],
    primaryKeys = [Constants.User.ID]
)
class User : Base {

    lateinit var email: String
    lateinit var name: String

    @Ignore
    constructor() {
    }

    constructor(id: String) : super(id) {
    }

    @Ignore
    private constructor(`in`: Parcel) : super(`in`) {
        email = `in`.toString()
        name = `in`.toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeString(email)
        parcel.writeString(name)
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}