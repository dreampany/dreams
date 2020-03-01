package com.dreampany.tools.data.model.crypto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.util.TimeUtilKt
import com.dreampany.tools.misc.Constants
import com.google.common.base.Objects
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 29/2/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@IgnoreExtraProperties
@Entity(
    indices = [Index(
        value = [Constants.Keys.Exchange.ID],
        unique = true
    )],
    primaryKeys = [Constants.Keys.Exchange.ID]
)
data class Exchange(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    var market: String = Constants.Default.STRING,
    @ColumnInfo(name = Constants.Keys.Exchange.FROM_SYMBOL)
    private var fromSymbol: String = Constants.Default.STRING,
    @ColumnInfo(name = Constants.Keys.Exchange.TO_SYMBOL)
    private var toSymbol: String = Constants.Default.STRING,
    var price: Double = Constants.Default.DOUBLE,
    @ColumnInfo(name = Constants.Keys.Exchange.VOLUME_24H)
    private var volume24h: Double = Constants.Default.DOUBLE,
    @ColumnInfo(name = Constants.Keys.Exchange.CHANGE_24H)
    private var change24h: Double = Constants.Default.DOUBLE,
    @ColumnInfo(name = Constants.Keys.Exchange.CHANGE_PCT_24H)
    private var changePct24h: Double = Constants.Default.DOUBLE
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
        val item = other as Exchange
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String {
        return "Exchange ($id) == $id"
    }

    @PropertyName(Constants.Keys.Exchange.FROM_SYMBOL)
    fun setFromSymbol(fromSymbol: String) {
        this.fromSymbol = fromSymbol
    }

    @PropertyName(Constants.Keys.Exchange.FROM_SYMBOL)
    fun getFromSymbol(): String {
        return fromSymbol
    }

    @PropertyName(Constants.Keys.Exchange.TO_SYMBOL)
    fun setToSymbol(toSymbol: String) {
        this.toSymbol = toSymbol
    }

    @PropertyName(Constants.Keys.Exchange.TO_SYMBOL)
    fun getToSymbol(): String {
        return toSymbol
    }

    @PropertyName(Constants.Keys.Exchange.VOLUME_24H)
    fun setVolume24h(volume24h: Double) {
        this.volume24h = volume24h
    }

    @PropertyName(Constants.Keys.Exchange.VOLUME_24H)
    fun getVolume24h(): Double {
        return volume24h
    }

    @PropertyName(Constants.Keys.Exchange.CHANGE_24H)
    fun setChange24h(change24h: Double) {
        this.change24h = change24h
    }

    @PropertyName(Constants.Keys.Exchange.CHANGE_24H)
    fun getChange24h(): Double {
        return change24h
    }

    @PropertyName(Constants.Keys.Exchange.CHANGE_PCT_24H)
    fun setChangePct24h(changePct24h: Double) {
        this.changePct24h = changePct24h
    }

    @PropertyName(Constants.Keys.Exchange.CHANGE_PCT_24H)
    fun getChangePct24h(): Double {
        return changePct24h
    }
}