package com.dreampany.match.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Ignore
import com.dreampany.frame.data.model.Base

/**
 * Created by Roman-372 on 6/26/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class User : Base {

    private var email: String? = null

    @Ignore
    constructor() {
    }

    private constructor(id: String) : super(id) {
    }

    @Ignore
    private constructor(`in`: Parcel) : super(`in`) {
        email = `in`.toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeString(email)
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