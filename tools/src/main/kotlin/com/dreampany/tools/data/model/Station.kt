package com.dreampany.tools.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.util.TimeUtilKt
import com.dreampany.tools.misc.Constants
import com.google.common.base.Objects
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-10-09
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@IgnoreExtraProperties
@Entity(
    indices = [Index(
        value = [Constants.Station.ID],
        unique = true
    )],
    primaryKeys = [Constants.Station.ID]
)
data class Station(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    @SerializedName(value = Constants.Station.Remote.CHANGE_UUID)
    @ColumnInfo(name = Constants.Station.CHANGE_UUID)
    private var changeUuid: String? = Constants.Default.NULL,
    @SerializedName(value = Constants.Station.Remote.STATION_UUID)
    @ColumnInfo(name = Constants.Station.STATION_UUID)
    private var stationUuid: String? = Constants.Default.NULL,
    var name: String? = Constants.Default.NULL,
    var url: String? = Constants.Default.NULL,
    var homepage: String? = Constants.Default.NULL,
    var favicon: String? = Constants.Default.NULL,
    var ip: String? = Constants.Default.NULL,
    var codec: String? = Constants.Default.NULL,
    var bitrate: Int = Constants.Default.INT,
    var tags: String? = Constants.Default.NULL,
    var country: String? = Constants.Default.NULL,

    @SerializedName(value = Constants.Station.Remote.COUNTRY_CODE)
    @ColumnInfo(name = Constants.Station.COUNTRY_CODE)
    private var countryCode: String? = Constants.Default.NULL,
    var state: String? = Constants.Default.NULL,
    var language: String? = Constants.Default.NULL,
    var votes: Int = Constants.Default.INT,

    @SerializedName(value = Constants.Station.Remote.NEGATIVE_VOTES)
    @ColumnInfo(name = Constants.Station.NEGATIVE_VOTES)
    private var negativeVotes: Int = Constants.Default.INT,

    @SerializedName(value = Constants.Station.Remote.CLICK_COUNT)
    @ColumnInfo(name = Constants.Station.CLICK_COUNT)
    private var clickCount: Int = Constants.Default.INT,

    @SerializedName(value = Constants.Station.Remote.CLICK_TREND)
    @ColumnInfo(name = Constants.Station.CLICK_TREND)
    private var clickTrend: Int = Constants.Default.INT,
    var hls: Boolean = Constants.Default.BOOLEAN,

    @SerializedName(value = Constants.Station.Remote.LAST_CHECK_OK)
    @ColumnInfo(name = Constants.Station.LAST_CHECK_OK)
    private var lastCheckOk: Boolean = Constants.Default.BOOLEAN,

    @SerializedName(value = Constants.Station.Remote.LAST_CHANGE_TIME)
    @ColumnInfo(name = Constants.Station.LAST_CHANGE_TIME)
    private var lastChangeTime: Long = Constants.Default.LONG,

    @SerializedName(value = Constants.Station.Remote.LAST_CHECK_TIME)
    @ColumnInfo(name = Constants.Station.LAST_CHECK_TIME)
    private var lastCheckTime: Long = Constants.Default.LONG,

    @SerializedName(value = Constants.Station.Remote.LAST_CHECK_OK_TIME)
    @ColumnInfo(name = Constants.Station.LAST_CHECK_OK_TIME)
    private var lastCheckOkTime: Long = Constants.Default.LONG,

    @SerializedName(value = Constants.Station.Remote.CLICK_TIMESTAMP)
    @ColumnInfo(name = Constants.Station.CLICK_TIMESTAMP)
    private var clickTimestamp: Long = Constants.Default.LONG


) : Base() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {

    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Station
        return Objects.equal(this.id, item.id)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

    override fun toString(): String {
        return "Station [$id] [$url] [$countryCode]"
    }

    @PropertyName(Constants.Station.CHANGE_UUID)
    fun setChangeUuid(changeUuid: String?) {
        this.changeUuid = changeUuid
    }

    @PropertyName(Constants.Station.CHANGE_UUID)
    fun getChangeUuid(): String? {
        return changeUuid
    }

    @PropertyName(Constants.Station.STATION_UUID)
    fun setStationUuid(stationUuid: String?) {
        this.stationUuid = stationUuid
    }

    @PropertyName(Constants.Station.STATION_UUID)
    fun getStationUuid(): String? {
        return stationUuid
    }

    @PropertyName(Constants.Station.COUNTRY_CODE)
    fun setCountryCode(countryCode: String?) {
        this.countryCode = countryCode
    }

    @PropertyName(Constants.Station.COUNTRY_CODE)
    fun getCountryCode(): String? {
        return countryCode
    }

    @PropertyName(Constants.Station.NEGATIVE_VOTES)
    fun setNegativeVotes(negativeVotes: Int) {
        this.negativeVotes = negativeVotes
    }

    @PropertyName(Constants.Station.NEGATIVE_VOTES)
    fun getNegativeVotes(): Int {
        return negativeVotes
    }

    @PropertyName(Constants.Station.CLICK_COUNT)
    fun setClickCount(clickCount: Int) {
        this.clickCount = clickCount
    }

    @PropertyName(Constants.Station.CLICK_COUNT)
    fun getClickCount(): Int {
        return clickCount
    }

    @PropertyName(Constants.Station.CLICK_TREND)
    fun setClickTrend(clickTrend: Int) {
        this.clickTrend = clickTrend
    }

    @PropertyName(Constants.Station.CLICK_TREND)
    fun getClickTrend(): Int {
        return clickTrend
    }

    @PropertyName(Constants.Station.LAST_CHECK_OK)
    fun setLastCheckOk(lastCheckOk: Boolean) {
        this.lastCheckOk = lastCheckOk
    }

    @PropertyName(Constants.Station.LAST_CHECK_OK)
    fun getLastCheckOk(): Boolean {
        return lastCheckOk
    }

    @PropertyName(Constants.Station.LAST_CHANGE_TIME)
    fun setLastChangeTime(lastChangeTime: Long) {
        this.lastChangeTime = lastChangeTime
    }

    @PropertyName(Constants.Station.LAST_CHANGE_TIME)
    fun getLastChangeTime(): Long {
        return lastChangeTime
    }

    @PropertyName(Constants.Station.LAST_CHECK_TIME)
    fun setLastCheckTime(lastCheckTime: Long) {
        this.lastCheckTime = lastCheckTime
    }

    @PropertyName(Constants.Station.LAST_CHECK_TIME)
    fun getLastCheckTime(): Long {
        return lastCheckTime
    }

    @PropertyName(Constants.Station.LAST_CHECK_OK_TIME)
    fun setLastCheckOkTime(lastCheckOkTime: Long) {
        this.lastCheckOkTime = lastCheckOkTime
    }

    @PropertyName(Constants.Station.LAST_CHECK_OK_TIME)
    fun getLastCheckOkTime(): Long {
        return lastCheckOkTime
    }

    @PropertyName(Constants.Station.CLICK_TIMESTAMP)
    fun setClickTimestamp(clickTimestamp: Long) {
        this.clickTimestamp = clickTimestamp
    }

    @PropertyName(Constants.Station.CLICK_TIMESTAMP)
    fun getClickTimestamp(): Long {
        return clickTimestamp
    }
}
