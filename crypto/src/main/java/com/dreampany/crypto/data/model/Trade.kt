package com.dreampany.crypto.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.util.Util
import com.dreampany.crypto.misc.constants.Constants
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 29/2/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@Entity(
    indices = [Index(
        value = [Constant.Keys.ID],
        unique = true
    )],
    primaryKeys = [Constant.Keys.ID]
)
data class Trade(
    override var time: Long = Constant.Default.LONG,
    override var id: String = Constant.Default.STRING,
    var exchange: String = Constant.Default.STRING,
    @ColumnInfo(name = Constants.Keys.Trade.FROM_SYMBOL)
    private var fromSymbol: String = Constant.Default.STRING,
    @ColumnInfo(name = Constants.Keys.Trade.TO_SYMBOL)
    private var toSymbol: String = Constant.Default.STRING,
    @ColumnInfo(name = Constants.Keys.Trade.VOLUME_24H)
    private var volume24h: Double = Constant.Default.DOUBLE,
    @ColumnInfo(name = Constants.Keys.Trade.VOLUME_24H_TO)
    private var volume24hTo: Double = Constant.Default.DOUBLE
) : Base() {

    @Ignore
    constructor() : this(time = Util.currentMillis()) {

    }

    constructor(id: String) : this(time = Util.currentMillis(), id = id) {

    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Trade
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String {
        return "Trade ($id) == $id"
    }

    //@PropertyName(AppConstants.Keys.Trade.FROM_SYMBOL)
    fun setFromSymbol(fromSymbol: String) {
        this.fromSymbol = fromSymbol
    }

   // @PropertyName(AppConstants.Keys.Trade.FROM_SYMBOL)
    fun getFromSymbol(): String {
        return fromSymbol
    }

    //@PropertyName(AppConstants.Keys.Trade.TO_SYMBOL)
    fun setToSymbol(toSymbol: String) {
        this.toSymbol = toSymbol
    }

    //@PropertyName(AppConstants.Keys.Trade.TO_SYMBOL)
    fun getToSymbol(): String {
        return toSymbol
    }

    //@PropertyName(AppConstants.Keys.Trade.VOLUME_24H)
    fun setVolume24h(volume24h: Double) {
        this.volume24h = volume24h
    }

    //@PropertyName(AppConstants.Keys.Trade.VOLUME_24H)
    fun getVolume24h(): Double {
        return volume24h
    }

    //@PropertyName(AppConstants.Keys.Trade.VOLUME_24H_TO)
    fun setVolume24hTo(volume24hTo: Double) {
        this.volume24hTo = volume24hTo
    }

    //@PropertyName(AppConstants.Keys.Trade.VOLUME_24H_TO)
    fun getVolume24hTo(): Double {
        return volume24hTo
    }
}