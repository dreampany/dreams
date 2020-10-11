package com.dreampany.tools.data.model.wifi

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.util.Util
import com.dreampany.tools.data.enums.wifi.Security
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 23/5/20
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
data class Wifi(
    override var time: Long = Constant.Default.LONG,
    override var id: String = Constant.Default.STRING,
    var ssid: String = Constant.Default.STRING,
    var bssid: String = Constant.Default.STRING,
    var capabilities: String? = Constant.Default.NULL,
    @Embedded
    var signal: Signal? = Constant.Default.NULL,
    var speed : Int = Constant.Default.INT
    /*,
    var level : Int = Constants.Default.INT,
    var frequency : Int = Constants.Default.INT,
    var channelWidth : Int = Constants.Default.INT,
    var centerFreq0 : Int = Constants.Default.INT,
    var centerFreq1 : Int = Constants.Default.INT,
    var timestamp : Long = Constants.Default.LONG,
    var venueName : String = Constants.Default.STRING,
    var operatorFriendlyName : String = Constants.Default.STRING,
    var carrierName : String = Constants.Default.STRING*/
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
        val item = other as Wifi
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String = "Wifi ($id) == $id"

    val security : Security
        get() = Security.findOne(capabilities)

    /* val is2GHz : Boolean
         get() = frequency > 2400 && frequency < 2500

     val is5GHz : Boolean
         get() = frequency > 4900 && frequency < 5900*/
}