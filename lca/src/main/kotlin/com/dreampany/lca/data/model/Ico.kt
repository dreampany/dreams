package com.dreampany.lca.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.frame.data.model.Base
import com.dreampany.frame.util.TimeUtilKt
import com.dreampany.lca.data.enums.IcoStatus
import com.dreampany.lca.misc.Constants
import kotlinx.android.parcel.Parcelize

/**
 * Created by Roman-372 on 8/2/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@Entity(
    indices = [Index(
        value = [Constants.Ico.ID],
        unique = true
    )],
    primaryKeys = [Constants.Ico.ID]
)
data class Ico(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    private var name: String? = Constants.Default.NULL,

    var imageUrl: String? = Constants.Default.NULL,
    var description: String? = Constants.Default.NULL,
    var websiteLink: String? = Constants.Default.NULL,
    var icoWatchListUrl: String? = Constants.Default.NULL,
    var startTime: String? = Constants.Default.NULL,
    var endTime: String? = Constants.Default.NULL,
    var timezone: String? = Constants.Default.NULL,
    var coinSymbol: String? = Constants.Default.NULL,
    var priceUSD: String? = Constants.Default.NULL,
    var allTimeRoi: String? = Constants.Default.NULL,
    var status: IcoStatus? = Constants.Default.NULL
) : Base() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {
    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {}

}