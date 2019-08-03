package com.dreampany.lca.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.frame.data.model.Base
import com.dreampany.frame.util.TimeUtilKt
import com.dreampany.lca.data.enums.Currency
import com.dreampany.lca.misc.Constants
import com.google.common.base.Objects
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Roman-372 on 8/2/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@IgnoreExtraProperties
@Entity(
    indices = [Index(
        value = [Constants.Quote.ID, Constants.Quote.CURRENCY],
        unique = true
    )],
    primaryKeys = [Constants.Quote.ID, Constants.Quote.CURRENCY]
)
data class Quote(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    var currency: Currency = Currency.USD,
    var price: Double = Constants.Default.DOUBLE,
    private var dayVolume: Double = Constants.Default.DOUBLE,
    private var marketCap: Double = Constants.Default.DOUBLE,
    private var hourChange: Double = Constants.Default.DOUBLE,
    private var dayChange: Double = Constants.Default.DOUBLE,
    private var weekChange: Double = Constants.Default.DOUBLE,
    private var lastUpdated: Long = 0
) : Base() {

    @Ignore
    constructor() : this(TimeUtilKt.currentMillis()) {
    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {}

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Quote
        return Objects.equal(item.id, id) && Objects.equal(item.currency, currency)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id, currency)
    }

    @PropertyName(Constants.Quote.DAY_VOLUME)
    fun setDayVolume(dayVolume: Double) {
        this.dayVolume = dayVolume
    }

    @PropertyName(Constants.Quote.DAY_VOLUME)
    fun getDayVolume(): Double {
        return dayVolume
    }

    @PropertyName(Constants.Quote.MARKET_CAP)
    fun setMarketCap(marketCap: Double) {
        this.marketCap = marketCap
    }

    @PropertyName(Constants.Quote.MARKET_CAP)
    fun getMarketCap(): Double {
        return marketCap
    }

    @PropertyName(Constants.Quote.HOUR_CHANGE)
    fun setHourChange(hourChange: Double) {
        this.hourChange = hourChange
    }

    @PropertyName(Constants.Quote.HOUR_CHANGE)
    fun getHourChange(): Double {
        return hourChange
    }

    @PropertyName(Constants.Quote.DAY_CHANGE)
    fun setDayChange(dayChange: Double) {
        this.dayChange = dayChange
    }

    @PropertyName(Constants.Quote.DAY_CHANGE)
    fun getDayChange(): Double {
        return dayChange
    }

    @PropertyName(Constants.Quote.WEEK_CHANGE)
    fun setWeekChange(weekChange: Double) {
        this.weekChange = weekChange
    }

    @PropertyName(Constants.Quote.WEEK_CHANGE)
    fun getWeekChange(): Double {
        return weekChange
    }

    @PropertyName(Constants.Quote.LAST_UPDATED)
    fun setLastUpdated(lastUpdated: Long) {
        this.lastUpdated = lastUpdated
    }

    @PropertyName(Constants.Quote.LAST_UPDATED)
    fun getLastUpdated(): Long {
        return lastUpdated
    }
}