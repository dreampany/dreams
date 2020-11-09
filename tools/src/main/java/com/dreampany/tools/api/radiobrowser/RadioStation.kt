package com.dreampany.tools.api.radiobrowser

import androidx.room.Ignore
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.util.Util
import com.dreampany.tools.api.radiobrowser.misc.Constants
import com.google.common.base.Objects
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-10-12
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class RadioStation(
    override var time: Long = Constant.Default.LONG,
    @SerializedName(value = Constants.Keys.Station.Remote.STATION_UUID)
    override var id: String = Constant.Default.STRING,
    @SerializedName(value = Constants.Keys.Station.Remote.CHANGE_UUID)
    var changeUuid: String? = Constant.Default.NULL,
    var name: String? = Constant.Default.NULL,
    var url: String? = Constant.Default.NULL,
    @SerializedName(value = Constants.Keys.Station.Remote.URL_RESOLVED)
    var urlResolved: String? = Constant.Default.NULL,
    var homepage: String? = Constant.Default.NULL,
    var favicon: String? = Constant.Default.NULL,
    var tags: String? = Constant.Default.NULL,
    var country: String? = Constant.Default.NULL,
    @SerializedName(value = Constants.Keys.Station.Remote.COUNTRY_CODE)
    var countryCode: String? = Constant.Default.NULL,
    var state: String? = Constant.Default.NULL,
    var language: String? = Constant.Default.NULL,
    var votes: Int = Constant.Default.INT,
    @SerializedName(value = Constants.Keys.Station.Remote.LAST_CHANGE_TIME)
    var lastChangeTime: String? = Constant.Default.NULL,
    var codec: String? = Constant.Default.NULL,
    var bitrate: Int = Constant.Default.INT,
    var hls: Int = Constant.Default.INT,
    @SerializedName(value = Constants.Keys.Station.Remote.LAST_CHECK_OK)
    var lastCheckOk: Int = Constant.Default.INT,
    @SerializedName(value = Constants.Keys.Station.Remote.LAST_CHECK_TIME)
    var lastCheckTime: String? = Constant.Default.NULL,
    @SerializedName(value = Constants.Keys.Station.Remote.LAST_CHECK_OK_TIME)
    var lastCheckOkTime: String? = Constant.Default.NULL,
    @SerializedName(value = Constants.Keys.Station.Remote.LAST_LOCAL_CHECK_TIME)
    var lastLocalCheckTime: String? = Constant.Default.NULL,
    @SerializedName(value = Constants.Keys.Station.Remote.CLICK_TIMESTAMP)
    var clickTimestamp: String? = Constant.Default.NULL,
    @SerializedName(value = Constants.Keys.Station.Remote.CLICK_COUNT)
    var clickCount: Int = Constant.Default.INT,
    @SerializedName(value = Constants.Keys.Station.Remote.CLICK_TREND)
    var clickTrend: Int = Constant.Default.INT
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
        val item = other as RadioStation
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String = "RadioStation [$name] [$url]"
}