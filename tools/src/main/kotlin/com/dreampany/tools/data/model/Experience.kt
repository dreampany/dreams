package com.dreampany.tools.data.model

import androidx.room.ColumnInfo
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.data.model.BaseParcel
import com.dreampany.tools.misc.Constants
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2020-01-11
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class Experience(
    @ColumnInfo(name = Constants.Keys.Experience.TIME)
    override var time: Long = Constants.Default.LONG,
    @ColumnInfo(name = Constants.Keys.Experience.ID)
    override var id: String = Constants.Default.STRING,
    var company: String? = Constants.Default.NULL,
    var location: String? = Constants.Default.NULL,
    var designation: String? = Constants.Default.NULL,
    var description: String? = Constants.Default.NULL,
    var from: Long = 0L,
    var to: Long = 0L
) : Base() {
}