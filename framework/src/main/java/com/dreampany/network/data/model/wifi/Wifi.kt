package com.dreampany.network.data.model.wifi

import com.dreampany.framework.data.model.BaseParcel
import com.dreampany.network.data.model.Network
import com.dreampany.network.misc.Constants
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 13/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class Wifi(
    var bssid: String = Constants.Default.STRING,
    var ssid: String = Constants.Default.STRING,
    var capabilities: String = Constants.Default.STRING
) : BaseParcel() {

    override fun hashCode(): Int = Objects.hashCode(bssid, ssid)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Wifi
        return Objects.equal(bssid, item.bssid) && Objects.equal(ssid, item.ssid)
    }
}