/*
package com.dreampany.hello.data.model

import androidx.room.Entity
import androidx.room.Index
import com.dreampany.framework.data.model.BaseParcel
import com.dreampany.framework.misc.constant.Constant
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ServerValue
import kotlinx.android.parcel.Parcelize

*/
/**
 * Created by roman on 12/13/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 *//*

@Parcelize
@IgnoreExtraProperties
@Entity(
    indices = [Index(
        value = [Constant.Keys.ID],
        unique = true
    )],
    primaryKeys = [Constant.Keys.ID]
)
data class Info(
    var id: String = Constant.Default.STRING,
    var timestamp: Map<String, String> = ServerValue.TIMESTAMP,
    var status : String? = Constant.Default.NULL,
    var level : Int = Constant.Default.INT
) : BaseParcel() {

}*/
