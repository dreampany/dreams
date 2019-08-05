package com.dreampany.tools.data.model

import androidx.room.Entity
import androidx.room.Index
import com.dreampany.frame.data.model.Base
import com.dreampany.tools.misc.Constants
import kotlinx.android.parcel.Parcelize

/**
 * Created by Roman-372 on 8/5/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@Entity(
    indices = [Index(
        value = [Constants.Note.ID],
        unique = true
    )],
    primaryKeys = [Constants.Note.ID]
)
data class Note(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    var title : String = Constants.Default.STRING,
    var description : String = Constants.Default.STRING,
    var tags: MutableList<String>? = Constants.Default.NULL
) : Base(){
}