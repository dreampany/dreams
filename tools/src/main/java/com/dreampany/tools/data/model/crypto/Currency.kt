package com.dreampany.tools.data.model.crypto

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.currentMillis
import com.google.common.base.Objects
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 11/18/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@IgnoreExtraProperties
@Entity(
    indices = [Index(
        value = [Constant.Keys.ID],
        unique = true
    )],
    primaryKeys = [Constant.Keys.ID]
)
data class Currency(
    override var time: Long = Constant.Default.LONG,
    override var id: String = Constant.Default.STRING,
    var name: String = Constant.Default.STRING,
    var sign: String = Constant.Default.STRING,
    var symbol: String = Constant.Default.STRING,
    var type: Type = Type.FIAT
) : Base() {

    @Parcelize
    enum class Type : Parcelable { FIAT, CRYPTO }

    @Ignore
    constructor() : this(time = currentMillis) {

    }

    constructor(id: String) : this(time = currentMillis, id = id) {

    }

    override fun hashCode(): Int = Objects.hashCode(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Coin
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String = "Currency: $id"

    val isFiat: Boolean get() = type == Type.FIAT

    val isCrypto: Boolean get() = type == Type.CRYPTO

    companion object {
        val USD = Currency(currentMillis, "2781", "United States Dollar", "$", "USD")
    }
}