package com.dreampany.hello.data.model

import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.currentMillis
import com.dreampany.hello.misc.Constants
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 12/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class Country(
    @ColumnInfo(name = Constants.Keys.Country.TIME)
    override var time: Long = Constant.Default.LONG,
    @ColumnInfo(name = Constants.Keys.Country.ID)
    override var id: String = Constant.Default.STRING,
    @ColumnInfo(name = Constants.Keys.Country.NAME)
    var name: String= Constant.Default.STRING,
    @ColumnInfo(name = Constants.Keys.Country.FLAG)
    var flag: String= Constant.Default.STRING
) : Base() {

    @Ignore
    constructor() : this(time = currentMillis) {

    }

    constructor(id: String) : this(time = currentMillis, id = id) {

    }

    override fun hashCode(): Int = Objects.hashCode(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Country
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String = "Country: $id"
}