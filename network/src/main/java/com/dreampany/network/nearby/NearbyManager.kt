package com.dreampany.network.nearby

import android.Manifest
import android.content.Context
import com.dreampany.network.misc.hasPermission
import com.dreampany.network.misc.hasPlayService
import com.dreampany.network.nearby.core.NearbyApi
import com.dreampany.network.nearby.core.Packets.Companion.dataPacket
import com.dreampany.network.nearby.model.Id
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 20/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class NearbyManager
@Inject constructor(
    context: Context
) : NearbyApi(context) {

    @Throws(Throwable::class)
    fun init(type: Type, serviceId: String, peerId: String, peerData: ByteArray?): Boolean {
        synchronized(guard) {
            this.type = type
            this.serviceId = serviceId
            this.peerId = peerId
            this.peerData = peerData

            if (!context.hasPlayService) {
                throw IllegalStateException("Google Play service is not available!")
            }

            if (!context.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                throw IllegalStateException("Location permission is missing!")
            }

            inited = true
        }
        return inited
    }

    fun start() {
        synchronized(guard) {
            super.startApi(type, serviceId, peerId)
        }
    }

    fun stop() {
        synchronized(guard) {
            super.stopApi()
        }
    }

    fun send(id: Id, data: ByteArray, timeout: Long) {
        synchronized(guard) {
            val packet = data.dataPacket
            sendPacket(id, packet, timeout)
        }
    }

}