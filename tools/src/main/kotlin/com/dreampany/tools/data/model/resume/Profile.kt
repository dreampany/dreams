package com.dreampany.tools.data.model.resume

import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.util.TimeUtilKt
import com.dreampany.tools.data.model.Note
import com.dreampany.tools.misc.Constants
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2020-01-11
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class Profile(
    @ColumnInfo(name = Constants.Keys.Profile.TIME)
    override var time: Long = Constants.Default.LONG,
    @ColumnInfo(name = Constants.Keys.Profile.ID)
    override var id: String = Constants.Default.STRING,
    @ColumnInfo(name = Constants.Keys.Profile.NAME)
    var name: String? = Constants.Default.NULL,
    @ColumnInfo(name = Constants.Keys.Profile.DESIGNATION)
    var designation: String? = Constants.Default.NULL,
    @ColumnInfo(name = Constants.Keys.Profile.PHONE)
    var phone: String? = Constants.Default.NULL,
    @ColumnInfo(name = Constants.Keys.Profile.EMAIL)
    var email: String? = Constants.Default.NULL,
    @ColumnInfo(name = Constants.Keys.Profile.CURRENT_ADDRESS)
    var currentAddress: String? = Constants.Default.NULL,
    @ColumnInfo(name = Constants.Keys.Profile.PERMANENT_ADDRESS)
    var permanentAddress: String? = Constants.Default.NULL
) : Base() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {

    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {

    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Note
        return Objects.equal(this.id, item.id)
    }
}