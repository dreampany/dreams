package com.dreampany.crypto.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.util.Util
import com.dreampany.crypto.misc.constants.AppConstants
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 29/2/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
//@IgnoreExtraProperties
@Entity(
    indices = [Index(
        value = [Constant.Keys.ID],
        unique = true
    )],
    primaryKeys = [Constant.Keys.ID]
)
data class Exchange(
    override var time: Long = Constant.Default.LONG,
    override var id: String = Constant.Default.STRING,
    var market: String = Constant.Default.STRING,
    @ColumnInfo(name = AppConstants.Keys.Exchange.FROM_SYMBOL)
    private var fromSymbol: String = Constant.Default.STRING,
    @ColumnInfo(name = AppConstants.Keys.Exchange.TO_SYMBOL)
    private var toSymbol: String = Constant.Default.STRING,
    var price: Double = Constant.Default.DOUBLE,
    @ColumnInfo(name = AppConstants.Keys.Exchange.VOLUME_24H)
    private var volume24h: Double = Constant.Default.DOUBLE,
    @ColumnInfo(name = AppConstants.Keys.Exchange.CHANGE_24H)
    private var change24h: Double = Constant.Default.DOUBLE,
    @ColumnInfo(name = AppConstants.Keys.Exchange.CHANGE_PCT_24H)
    private var changePct24h: Double = Constant.Default.DOUBLE
) : Base() {

    @Ignore
    constructor() : this(time = Util.currentMillis()) {

    }

    constructor(id: String) : this(time = Util.currentMillis(), id = id) {

    }

    override fun hashCode(): Int = Objects.hashCode(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Exchange
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String = "Exchange ($id) == $id"

    //@PropertyName(AppConstants.Keys.Exchange.FROM_SYMBOL)
    fun setFromSymbol(fromSymbol: String) {
        this.fromSymbol = fromSymbol
    }

    //@PropertyName(AppConstants.Keys.Exchange.FROM_SYMBOL)
    fun getFromSymbol(): String {
        return fromSymbol
    }

    //@PropertyName(AppConstants.Keys.Exchange.TO_SYMBOL)
    fun setToSymbol(toSymbol: String) {
        this.toSymbol = toSymbol
    }

    //@PropertyName(AppConstants.Keys.Exchange.TO_SYMBOL)
    fun getToSymbol(): String {
        return toSymbol
    }

    //@PropertyName(AppConstants.Keys.Exchange.VOLUME_24H)
    fun setVolume24h(volume24h: Double) {
        this.volume24h = volume24h
    }

    //@PropertyName(AppConstants.Keys.Exchange.VOLUME_24H)
    fun getVolume24h(): Double {
        return volume24h
    }

    //@PropertyName(AppConstants.Keys.Exchange.CHANGE_24H)
    fun setChange24h(change24h: Double) {
        this.change24h = change24h
    }

    //@PropertyName(AppConstants.Keys.Exchange.CHANGE_24H)
    fun getChange24h(): Double {
        return change24h
    }

    //@PropertyName(AppConstants.Keys.Exchange.CHANGE_PCT_24H)
    fun setChangePct24h(changePct24h: Double) {
        this.changePct24h = changePct24h
    }

    //@PropertyName(AppConstants.Keys.Exchange.CHANGE_PCT_24H)
    fun getChangePct24h(): Double {
        return changePct24h
    }
}