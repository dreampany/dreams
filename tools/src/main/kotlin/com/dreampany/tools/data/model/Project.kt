package com.dreampany.tools.data.model

import androidx.room.ColumnInfo
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
data class Project(
    @ColumnInfo(name = Constants.Keys.Project.TIME)
    override var time: Long = Constants.Default.LONG,
    @ColumnInfo(name = Constants.Keys.Project.ID)
    override var id: String = Constants.Default.STRING,
    var name: String? = null,
    val description: String? = null,
    val from: Long = 0L,
    val to: Long = 0L
) : Base() {
}