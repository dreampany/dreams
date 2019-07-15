package com.dreampany.cast.data.model

import androidx.room.Entity
import androidx.room.Index
import com.dreampany.cast.misc.Constants
import com.dreampany.frame.data.model.BaseKt
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

/**
 * Created by Roman-372 on 6/26/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Entity(
    indices = [Index(
        value = [Constants.User.ID],
        unique = true
    )],
    primaryKeys = [Constants.User.ID]
)
@IgnoreExtraProperties
@Parcelize
data class User(
    override val id: String,
    override val time: Long
) : BaseKt() {

    lateinit var email: String
    lateinit var name: String

}