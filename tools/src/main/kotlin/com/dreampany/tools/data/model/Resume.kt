package com.dreampany.tools.data.model

import androidx.room.Entity
import androidx.room.Index
import com.dreampany.framework.data.model.Base
import com.dreampany.tools.misc.Constants
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2020-01-11
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@Entity(
    indices = [Index(
        value = [Constants.Keys.Resume.ID],
        unique = true
    )],
    primaryKeys = [Constants.Keys.Resume.ID]
)
data class Resume(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING
) : Base() {
}