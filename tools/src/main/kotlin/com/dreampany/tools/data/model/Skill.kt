package com.dreampany.tools.data.model

import androidx.room.ColumnInfo
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.data.model.BaseParcel
import com.dreampany.tools.misc.Constants
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2020-01-12
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class Skill(
    @ColumnInfo(name = Constants.Keys.Skill.TIME)
    override var time: Long = Constants.Default.LONG,
    @ColumnInfo(name = Constants.Keys.Skill.ID)
    override var id: String = Constants.Default.STRING,
    val title: String? = Constants.Default.NULL
) : Base() {
}