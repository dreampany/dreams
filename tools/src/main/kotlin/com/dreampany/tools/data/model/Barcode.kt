package com.dreampany.tools.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.frame.data.model.Base
import com.dreampany.frame.util.TimeUtilKt
import com.dreampany.tools.data.enums.BarcodeFormat
import com.dreampany.tools.data.enums.BarcodeType
import com.dreampany.tools.misc.Constants
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-08-04
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@Entity(
    indices = [Index(
        value = [Constants.Barcode.ID],
        unique = true
    )],
    primaryKeys = [Constants.Barcode.ID]
)
data class Barcode(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    var format: BarcodeFormat = BarcodeFormat.DEFAULT,
    var type: BarcodeType = BarcodeType.DEFAULT
    ) : Base() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {

    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {

    }

}