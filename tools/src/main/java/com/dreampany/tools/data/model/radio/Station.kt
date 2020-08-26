package com.dreampany.tools.data.model.radio

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.constant.Constants
import com.dreampany.framework.misc.util.Util
import com.dreampany.tools.misc.constants.RadioConstants
import com.google.common.base.Objects
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 15/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@IgnoreExtraProperties
@Entity(
    indices = [Index(
        value = [RadioConstants.Keys.Station.ID],
        unique = true
    )],
    primaryKeys = [RadioConstants.Keys.Station.ID]
)
data class Station(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    @SerializedName(value = RadioConstants.Keys.Station.Remote.CHANGE_UUID)
    @ColumnInfo(name = RadioConstants.Keys.Station.CHANGE_UUID)
    private var changeUuid: String? = Constants.Default.NULL,
    var name: String? = Constants.Default.NULL,
    var url: String? = Constants.Default.NULL,
    var homepage: String? = Constants.Default.NULL,
    var favicon: String? = Constants.Default.NULL,
    var ip: String? = Constants.Default.NULL,
    var codec: String? = Constants.Default.NULL,
    var bitrate: Int = Constants.Default.INT,
    var tags: String? = Constants.Default.NULL,
    var country: String? = Constants.Default.NULL,

    @SerializedName(value = RadioConstants.Keys.Station.Remote.COUNTRY_CODE)
    @ColumnInfo(name = RadioConstants.Keys.Station.COUNTRY_CODE)
    private var countryCode: String? = Constants.Default.NULL,
    var state: String? = Constants.Default.NULL,
    var language: String? = Constants.Default.NULL,
    var votes: Int = Constants.Default.INT,

    @SerializedName(value = RadioConstants.Keys.Station.Remote.NEGATIVE_VOTES)
    @ColumnInfo(name = RadioConstants.Keys.Station.NEGATIVE_VOTES)
    private var negativeVotes: Int = Constants.Default.INT,

    @SerializedName(value = RadioConstants.Keys.Station.Remote.CLICK_COUNT)
    @ColumnInfo(name = RadioConstants.Keys.Station.CLICK_COUNT)
    private var clickCount: Int = Constants.Default.INT,

    @SerializedName(value = RadioConstants.Keys.Station.Remote.CLICK_TREND)
    @ColumnInfo(name = RadioConstants.Keys.Station.CLICK_TREND)
    private var clickTrend: Int = Constants.Default.INT,
    var hls: Boolean = Constants.Default.BOOLEAN,

    @SerializedName(value = RadioConstants.Keys.Station.Remote.LAST_CHECK_OK)
    @ColumnInfo(name = RadioConstants.Keys.Station.LAST_CHECK_OK)
    private var lastCheckOk: Boolean = Constants.Default.BOOLEAN,

    @SerializedName(value = RadioConstants.Keys.Station.Remote.LAST_CHANGE_TIME)
    @ColumnInfo(name = RadioConstants.Keys.Station.LAST_CHANGE_TIME)
    private var lastChangeTime: Long = Constants.Default.LONG,

    @SerializedName(value = RadioConstants.Keys.Station.Remote.LAST_CHECK_TIME)
    @ColumnInfo(name = RadioConstants.Keys.Station.LAST_CHECK_TIME)
    private var lastCheckTime: Long = Constants.Default.LONG,

    @SerializedName(value = RadioConstants.Keys.Station.Remote.LAST_CHECK_OK_TIME)
    @ColumnInfo(name = RadioConstants.Keys.Station.LAST_CHECK_OK_TIME)
    private var lastCheckOkTime: Long = Constants.Default.LONG,

    @SerializedName(value = RadioConstants.Keys.Station.Remote.CLICK_TIMESTAMP)
    @ColumnInfo(name = RadioConstants.Keys.Station.CLICK_TIMESTAMP)
    private var clickTimestamp: Long = Constants.Default.LONG


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
        val item = other as Station
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String {
        return "Station [$id] [$url] [$countryCode]"
    }

    @PropertyName(RadioConstants.Keys.Station.CHANGE_UUID)
    fun setChangeUuid(changeUuid: String?) {
        this.changeUuid = changeUuid
    }

    @PropertyName(RadioConstants.Keys.Station.CHANGE_UUID)
    fun getChangeUuid(): String? {
        return changeUuid
    }

    @PropertyName(RadioConstants.Keys.Station.COUNTRY_CODE)
    fun setCountryCode(countryCode: String?) {
        this.countryCode = countryCode
    }

    @PropertyName(RadioConstants.Keys.Station.COUNTRY_CODE)
    fun getCountryCode(): String? {
        return countryCode
    }

    @PropertyName(RadioConstants.Keys.Station.NEGATIVE_VOTES)
    fun setNegativeVotes(negativeVotes: Int) {
        this.negativeVotes = negativeVotes
    }

    @PropertyName(RadioConstants.Keys.Station.NEGATIVE_VOTES)
    fun getNegativeVotes(): Int {
        return negativeVotes
    }

    @PropertyName(RadioConstants.Keys.Station.CLICK_COUNT)
    fun setClickCount(clickCount: Int) {
        this.clickCount = clickCount
    }

    @PropertyName(RadioConstants.Keys.Station.CLICK_COUNT)
    fun getClickCount(): Int {
        return clickCount
    }

    @PropertyName(RadioConstants.Keys.Station.CLICK_TREND)
    fun setClickTrend(clickTrend: Int) {
        this.clickTrend = clickTrend
    }

    @PropertyName(RadioConstants.Keys.Station.CLICK_TREND)
    fun getClickTrend(): Int {
        return clickTrend
    }

    @PropertyName(RadioConstants.Keys.Station.LAST_CHECK_OK)
    fun setLastCheckOk(lastCheckOk: Boolean) {
        this.lastCheckOk = lastCheckOk
    }

    @PropertyName(RadioConstants.Keys.Station.LAST_CHECK_OK)
    fun getLastCheckOk(): Boolean {
        return lastCheckOk
    }

    @PropertyName(RadioConstants.Keys.Station.LAST_CHANGE_TIME)
    fun setLastChangeTime(lastChangeTime: Long) {
        this.lastChangeTime = lastChangeTime
    }

    @PropertyName(RadioConstants.Keys.Station.LAST_CHANGE_TIME)
    fun getLastChangeTime(): Long {
        return lastChangeTime
    }

    @PropertyName(RadioConstants.Keys.Station.LAST_CHECK_TIME)
    fun setLastCheckTime(lastCheckTime: Long) {
        this.lastCheckTime = lastCheckTime
    }

    @PropertyName(RadioConstants.Keys.Station.LAST_CHECK_TIME)
    fun getLastCheckTime(): Long {
        return lastCheckTime
    }

    @PropertyName(RadioConstants.Keys.Station.LAST_CHECK_OK_TIME)
    fun setLastCheckOkTime(lastCheckOkTime: Long) {
        this.lastCheckOkTime = lastCheckOkTime
    }

    @PropertyName(RadioConstants.Keys.Station.LAST_CHECK_OK_TIME)
    fun getLastCheckOkTime(): Long {
        return lastCheckOkTime
    }

    @PropertyName(RadioConstants.Keys.Station.CLICK_TIMESTAMP)
    fun setClickTimestamp(clickTimestamp: Long) {
        this.clickTimestamp = clickTimestamp
    }

    @PropertyName(RadioConstants.Keys.Station.CLICK_TIMESTAMP)
    fun getClickTimestamp(): Long {
        return clickTimestamp
    }
}
