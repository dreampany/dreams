package com.dreampany.lca.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.frame.data.model.Base
import com.dreampany.frame.util.TimeUtilKt
import com.dreampany.lca.misc.Constants
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-07-24
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@Entity(
    indices = [Index(
        value = [Constants.SourceInfo.ID],
        unique = true
    )],
    primaryKeys = [Constants.SourceInfo.ID]
)
data class SourceInfo(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    private var name: String = Constants.Default.STRING,
    private var language: String = Constants.Default.STRING,
    @ColumnInfo(name = Constants.SourceInfo.IMAGE_URL)
    private var imageUrl: String = Constants.Default.STRING

) : Base() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {
    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {}
}