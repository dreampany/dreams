package com.dreampany.network.nearby.core

import android.content.Context
import com.dreampany.network.misc.*
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.google.common.collect.Maps
import timber.log.Timber
import java.util.concurrent.Executor

/**
 * Created by roman on 7/2/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class Connection(
    private val context: Context,
    private val executor: Executor,
    private val callback: Callback
) : ConnectionLifecycleCallback() {

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

    @Volatile
    private lateinit var requestThread: Runner

    init {
        client = Nearby.getConnectionsClient(context.applicationContext)

        endpoints = HashBiMap.create()
        states = Maps.newConcurrentMap()
        directs = Maps.newConcurrentMap()
        requestTries = Maps.newConcurrentMap()
        pendingEndpoints = SmartQueue()
    }

    override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
        Timber.v("Connection Initiated endpoint: %s", endpointId)
        val peerId = info.endpointName
        endpoints[peerId] = endpointId
    }

    override fun onConnectionResult(endpointId: String, resolution: ConnectionResolution) {

    }

    override fun onDisconnected(endpointId: String) {


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
        startDiscovery()
    }


    /* private */
    private val String.peerId: String? get() = endpoints.inverse().get(this)

    private val String.endpointId: String?
        get() {
            val endpointId = endpoints.get(this)
            if (endpointId == null) return null
            if (states.get(endpointId) != State.ACCEPTED) return null
            return endpointId
        }

    private fun peerIdOf(endpointId: String): String? = endpointId.peerId

    /* discovery callback for getting advertised devices */
    private val discoveryCallback = object : EndpointDiscoveryCallback() {
        override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
            val serviceId = info.serviceId
            if (this@Connection.serviceId != serviceId) {
                Timber.e(
                    "Unknown [ServiceId-EndpointId]:[%s-%s]",
                    serviceId,
                    info.endpointName
                )
                return
            }

            val peerId = info.endpointName
            if (!peerId.isValidPeerId) return

            Timber.v("EndpointFound [EndpointId-PeerId]:[%s-%s]", endpointId, peerId)

            /* priority works: remove old endpoints if exists */
            if (endpoints.containsKey(peerId)) {
                val oldEndpointId = endpoints.remove(peerId)
                states.remove(oldEndpointId)
                directs.remove(oldEndpointId)
                pendingEndpoints.remove(oldEndpointId)
            }

            endpoints[peerId] = endpointId
            states[endpointId] = State.FOUND
            pendingEndpoints.insertLastUniquely(endpointId)
            requestTries.put(endpointId, 0)
            executor.execute({ startRequestThread() })
        }

        override fun onEndpointLost(endpointId: String) {
            val peerId = endpointId.peerId
            if (peerId == null) {
                Timber.v("EndpointLost [EndpointId]:[%s]", endpointId)
                return
            }

            Timber.v("EndpointLost [EndpointId-PeerId]:[%s-%s]", endpointId, peerId)

            endpoints.remove(peerId)
            states.put(endpointId, State.LOST)
            directs.remove(endpointId)
            pendingEndpoints.remove(endpointId)
            requestTries.remove(endpointId)
            executor.execute {
                callback.onConnection(peerId, false)
            }
        }
    }


    private fun startAdvertising() {
        synchronized(guard) {
            if (started.not() && advertising) return

            Timber.v("Advertising [ServiceId-PeerId]:[%s-%s]", serviceId, peerId)
            val serviceId = serviceId ?: return
            val peerId = peerId ?: return

            client.startAdvertising(peerId, serviceId, this, advertisingOptions)
                .addOnFailureListener {
                    advertising = false
                    Timber.e(
                        it,
                        "Failure Advertising [ServiceId-PeerId]:[%s-%s]",
                        serviceId,
                        peerId
                    )
                }.addOnSuccessListener {
                    advertising = true
                    Timber.v(
                        "Success Advertising [ServiceId-PeerId]:[%s-%s]",
                        serviceId,
                        peerId
                    )
                }
        }
    }

    private fun stopAdvertising() {
        synchronized(guard) {
            if (advertising.not()) return
            advertising = false
            client.stopAdvertising()
        }
    }

    private fun startDiscovery() {
        synchronized(guard) {
            if (started.not() && discovering) return

            Timber.v("Discovering [ServiceId-PeerId]:[%s-%s]", serviceId, peerId)
            val serviceId = serviceId ?: return

            client.startDiscovery(serviceId, discoveryCallback, discoveryOptions)
                .addOnFailureListener {
                    discovering = false
                    Timber.e(
                        it,
                        "Failure Discovering [ServiceId-PeerId]:[%s-%s]",
                        serviceId,
                        peerId
                    )
                }.addOnSuccessListener {
                    discovering = true
                    Timber.v(
                        "Success Discovering [ServiceId-PeerId]:[%s-%s]",
                        serviceId,
                        peerId
                    )
                }
        }
    }

    private fun stopDiscovery() {
        synchronized(guard) {
            if (discovering.not()) return
            discovering = false
            client.stopDiscovery()
        }
    }

    private fun startRequestThread() {
        synchronized(guard) {
            if (!::requestThread.isInitialized || !requestThread.running) {
                requestThread = RequestThread(this)
                requestThread.start()
            }
            requestThread.notifyRunner()
        }
    }

    private fun stopRequestThread() {
        synchronized(guard) {
            if (::requestThread.isInitialized) {
                requestThread.stop()
            }
        }
    }

    /* request thread */
    class RequestThread(val connection: Connection) : Runner() {

        @Throws(InterruptedException::class)
        override fun looping(): Boolean {

            val endpointId: String? = connection.pendingEndpoints.pollFirst()
            val peerId = endpointId?.let { connection.peerIdOf(it) }

            Timber.v("RequestThread [EndpointId-PeerId]:[%s-%s]", endpointId, peerId)

            if (endpointId == null || peerId == null) {
                waitRunner(wait)
                wait += delayS
                return true
            }

            wait = delayS

            val state = connection.states.get(endpointId)

            /* already requested endpoint */
            if (state != null) {
                Timber.v("State [EndpointId-PeerId-State]:[%s-%s-%s]", endpointId, peerId, state)

                when (state) {
                    State.REQUESTING,
                    State.ERROR-> return true

                    State.ALREADY_CONNECTED-> {
                        connection.executor.execute {
                            connection.callback.onConnection(peerId, true)
                        }
                        return true
                    }
                }
            }

            /* incoming endpoints: so don't make request on it */
            if (connection.directs.valueOf(endpointId)) {
                return true
            }

            if (!times.containsKey(endpointId)) {
                times[endpointId] = currentMillis
            }

            if (!delays.containsKey(endpointId)) {
                delays[endpointId] = Utils.nextRand(2, 5) * delayS
            }

            if (!connection.requestTries.containsKey(endpointId)) {
                connection.requestTries.put(endpointId, 0)
            }

            val readyToRequest = times.timeOf(endpointId).isExpired(delays.valueOf(endpointId))
            Timber.v("Request Attempt %s-%s", endpointId, readyToRequest)

            if (readyToRequest) {

            }else {
                connection.pendingEndpoints.insertLastUniquely(endpointId)
            }

            waitRunner(delayS)
            return true
        }

    }

}