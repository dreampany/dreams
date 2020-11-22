package com.dreampany.tools.data.model.crypto

import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.currentMillis
import com.dreampany.tools.misc.constants.Constants
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 11/15/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class Platform(
    @ColumnInfo(name = Constants.Keys.Platform.TIME)
    override var time: Long = Constant.Default.LONG,
    @ColumnInfo(name = Constants.Keys.Platform.ID)
    override var id: String = Constant.Default.STRING,
    @ColumnInfo(name = Constants.Keys.Platform.NAME)
    var name: String = Constant.Default.STRING,
    @ColumnInfo(name = Constants.Keys.Platform.SYMBOL)
    var symbol: String = Constant.Default.STRING,
    @ColumnInfo(name = Constants.Keys.Platform.SLUG)
    var slug: String = Constant.Default.STRING,
    @ColumnInfo(name = Constants.Keys.Platform.TOKEN_ADDRESS)
    var tokenAddress: String = Constant.Default.STRING
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
        val item = other as Platform
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String = "Platform: $id"

}