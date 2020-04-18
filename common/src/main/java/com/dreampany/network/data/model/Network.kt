package com.dreampany.network.data.model

import android.os.Parcelable
import android.text.TextUtils
import androidx.room.Entity
import androidx.room.Index
import com.dreampany.network.misc.Constants
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by Roman-372 on 6/27/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

@Parcelize
@Entity(
    indices = [Index(value = [Constants.Network.BSSID, Constants.Network.SSID], unique = true)],
    primaryKeys = [Constants.Network.BSSID, Constants.Network.SSID]
)
data class Network(
    val type: Type,
    var bssid: String = Constants.Default.STRING,
    var ssid: String = Constants.Default.STRING,
    var capabilities: String = Constants.Default.STRING,
    var enabled: Boolean = Constants.Default.BOOLEAN,
    var connected: Boolean = Constants.Default.BOOLEAN,
    var internet: Boolean = Constants.Default.BOOLEAN
) : Parcelable {

    enum class Type {
        DEFAULT, WIFI, HOTSPOT, BLUETOOTH
    }

    enum class Security {
        WEP, PSK, EAP
    }

    override fun hashCode(): Int {
        return Objects.hashCode(bssid, ssid)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Network
        return Objects.equal(bssid, item.bssid) && Objects.equal(ssid, item.ssid)
    }

    override fun toString(): String {
        val builder = StringBuilder("network#")
        builder.append(" type: ").append(type)
        builder.append(" bssid: ").append(bssid)
        builder.append(" ssid: ").append(ssid)
        builder.append(" open: ").append(isOpen())
        builder.append(" enabled: ").append(enabled)
        builder.append(" connected: ").append(connected)
        builder.append(" internet: ").append(internet)
        return builder.toString()
    }

    fun isOpen(): Boolean {
        return if (!TextUtils.isEmpty(capabilities)) {
            !(capabilities.contains(Security.WEP.name) ||
                    capabilities.contains(Security.PSK.name) ||
                    capabilities.contains(Security.EAP.name))
        } else true
    }
}