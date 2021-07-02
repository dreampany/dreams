package com.dreampany.network.nearby.core

import android.content.Context
import com.dreampany.network.misc.SmartQueue
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.google.common.collect.Maps
import timber.log.Timber

/**
 * Created by roman on 7/2/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class Connection(
    private val context: Context,
) {

    private enum class State {
        FOUND, LOST, REQUESTING, REQUEST_SUCCESS, REQUEST_FAILED,
        INITIATED, ALREADY_CONNECTED, ACCEPTED, REJECTED, ERROR, DISCONNECTED
    }

    interface Callback {
        fun onConnection(peerId: String, connected: Boolean)
        fun onPayload(peerId: String, payload: Payload)
        fun onPayloadStatus(peerId: String, update: PayloadTransferUpdate)
    }

    private val guard = Object()

    private var client: ConnectionsClient
    private lateinit var advertisingOptions: AdvertisingOptions
    private lateinit var discoveryOptions: DiscoveryOptions

    private var strategy: Strategy? = null
    private var serviceId: String? = null
    private var peerId: String? = null

    @Volatile
    private var advertising = false

    @Volatile
    private var discovering = false

    @Volatile
    private var started = false

    //private val cache: BiMap<Long, String>
    private val endpoints: BiMap<String, String> // peerId to endpointId
    private val states: MutableMap<String, State> // endpointId to State
    private val directs: MutableMap<String, Boolean>  // endpointId to directs (incoming = true or outgoing = false)
    private val requestTries: MutableMap<String, Int>
    private val pendingEndpoints: SmartQueue<String>
    private val MAX_TRY = 5

    init {
        client = Nearby.getConnectionsClient(context.applicationContext)

        endpoints = HashBiMap.create()
        states = Maps.newConcurrentMap()
        directs = Maps.newConcurrentMap()
        requestTries = Maps.newConcurrentMap()
        pendingEndpoints = SmartQueue()
    }

    fun start(strategy: Strategy, serviceId: String, peerId: String) {
        synchronized(guard) {
            if (started) return
            this.strategy = strategy
            this.serviceId = serviceId
            this.peerId = peerId
            started = true

            Timber.v("Starting Nearby Connection %s", strategy.toString())
            advertisingOptions = AdvertisingOptions.Builder().setStrategy(strategy).build()
            discoveryOptions = DiscoveryOptions.Builder().setStrategy(strategy).build()
        }

        startAdvertising()
    }


    /* private */
    private fun startAdvertising() {
        synchronized(guard) {
            if (advertising) {
                return
            }
            Timber.v("Advertising fired for ServiceId (%s) - Peer (%s)", serviceId, peerId)
            val serviceId = serviceId ?: return
            val peerId = peerId ?: return

        }
    }
}