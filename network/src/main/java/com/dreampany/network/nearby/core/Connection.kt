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
 * Created by roman on 19/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Connection(
    private val context: Context,
    private val executor: Executor,
    private val strategy: Strategy,
    private val serviceId: Long,
    private val peerId: Long,
    private val callback: Callback
) : ConnectionLifecycleCallback() {

    private enum class State {
        FOUND, LOST, REQUESTING, REQUEST_SUCCESS, REQUEST_FAILED,
        INITIATED, ACCEPTED, REJECTED, DISCONNECTED
    }

    interface Callback {
        fun onConnection(peerId: Long, connected: Boolean)
        fun onPayload(peerId: Long, payload: Payload)
        fun onPayloadStatus(peerId: Long, status: PayloadTransferUpdate)
    }

    private val guard = Object()

    private var client: ConnectionsClient
    private var advertisingOptions: AdvertisingOptions
    private var discoveryOptions: DiscoveryOptions
    //private val discoveryCallback: DiscoveryCallback

    @Volatile
    private var advertising = false

    @Volatile
    private var discovering = false

    @Volatile
    private var started = false

    private val cache: BiMap<Long, String>
    private val endpoints: BiMap<Long, String> // peerId to endpointId
    private val states: MutableMap<String, State> // endpointId to State
    private val directs: MutableMap<String, Boolean>  // endpointId to directs (incoming = true or outgoing = false)
    private val pendingEndpoints: SmartQueue<String>
    private val requestTries: MutableMap<String, Int>
    private val MAX_TRY = 5

    @Volatile
    private lateinit var requestThread: Runner

    init {
        client = Nearby.getConnectionsClient(context.applicationContext)
        advertisingOptions = AdvertisingOptions.Builder().setStrategy(strategy).build()
        discoveryOptions = DiscoveryOptions.Builder().setStrategy(strategy).build()

        cache = HashBiMap.create()
        endpoints = HashBiMap.create()
        states = Maps.newConcurrentMap()
        directs = Maps.newConcurrentMap()
        pendingEndpoints = SmartQueue()
        requestTries = Maps.newConcurrentMap()
    }

    override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
        val peerId = info.endpointName.long
        endpoints[peerId] = endpointId
        states[endpointId] = State.INITIATED
        directs[endpointId] = info.isIncomingConnection
        client.acceptConnection(endpointId, payloadCallback)
    }

    override fun onConnectionResult(endpointId: String, resolution: ConnectionResolution) {

        val accepted = resolution.status.statusCode == ConnectionsStatusCodes.STATUS_OK
        //TODO STATUS_CONNECTION_REJECTED
        Timber.v("First Connection Result endpoint: %s accepted %s", endpointId, accepted)
        states[endpointId] = if (accepted) State.ACCEPTED else State.REJECTED

        if (accepted) {
            pendingEndpoints.remove(endpointId)
            val peerId = peerIdOf(endpointId)
            executor.execute({ callback.onConnection(peerId, true) })
        } else {
            pendingEndpoints.insertLastUniquely(endpointId)
            executor.execute({ startRequestThread() })
        }
    }

    override fun onDisconnected(endpointId: String) {
        Timber.v("First Disconnected endpoint: %s", endpointId)
        states.put(endpointId, State.DISCONNECTED)
        pendingEndpoints.insertLastUniquely(endpointId)
        val peerId = peerIdOf(endpointId)
        executor.execute {
            callback.onConnection(peerId, false)
            startRequestThread()
        }
    }

    fun getPeerId(): Long = peerId

    fun start() {
        synchronized(guard) {
            started = true
            startAdvertising()
            startDiscovery()
        }
    }

    fun stop() {
        synchronized(guard) {
            started = false
            stopRequestThread()
            stopAdvertising()
            stopDiscovery()
        }
    }

    fun requireRestart(
        strategy: Strategy,
        serviceId: Long,
        peerId: Long
    ): Boolean = this.strategy != strategy ||
            this.serviceId != serviceId ||
            this.peerId != peerId || !started

    private fun startAdvertising() {
        synchronized(guard) {
            if (advertising) {
                return
            }
            Timber.v("Advertising fired for Peer (%d) - ServiceId (%d)", peerId, serviceId)
            client.startAdvertising(peerId.string, serviceId.string, this, advertisingOptions)
                .addOnSuccessListener { ignored: Void ->
                    advertising = true
                    Timber.v("Success Advertising of Peer (%d) - ServiceId (%d)", peerId, serviceId)
                }.addOnFailureListener { error: Exception ->
                    advertising = false
                    Timber.e(
                        "Error in Advertising of Peer (%d) - ServiceId (%d) - %s", peerId,
                        serviceId,
                        error.message
                    )
                }
        }
    }

    private fun stopAdvertising() {
        synchronized(guard) {
            advertising = false
            client.stopAdvertising()
        }
    }

    private fun startDiscovery() {
        synchronized(guard) {
            if (discovering) {
                return
            }
            Timber.v("Discovering fired for Peer (%d) - ServiceId (%d)", peerId, serviceId)
            client.startDiscovery(serviceId.string, discoveryCallback, discoveryOptions)
                .addOnSuccessListener { ignored: Void ->
                    discovering = true
                    Timber.v("Success Discovering Peer (%d) - ServiceId (%d)", peerId, serviceId)
                }
                .addOnFailureListener { error: Exception ->
                    discovering = false
                    Timber.e(
                        "Error Discovering Peer (%d) - ServiceId (%d) - %s",
                        peerId,
                        serviceId,
                        error.message
                    )
                }
        }
    }

    private fun stopDiscovery() {
        synchronized(guard) {
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

    private fun requestConnection(endpointId: String) {
        Timber.v("Requesting Connection: %s state[%s]", endpointId, states[endpointId])
        client.requestConnection(peerId.string, endpointId, this)
            .addOnSuccessListener { ignored: Void ->
                Timber.v("Request Connection succeed for (%s)", endpointId)
                states[endpointId] = State.REQUEST_SUCCESS
                pendingEndpoints.remove(endpointId)
                requestTries.remove(endpointId)
            }.addOnFailureListener { error: java.lang.Exception ->
                Timber.e("Request Connection error (%s) - %s", endpointId, error.message)
                Timber.e(error)
                states[endpointId] = State.REQUEST_FAILED
                pendingEndpoints.insertLastUniquely(endpointId)
                startRequestThread()
            }
    }

    private fun peerIdOf(endpointId: String): Long {
        return endpoints.inverse().get(endpointId) ?: 0L
    }

    private val Long.string: String
        get() {
            var result = cache.get(this)
            if (result == null) {
                result = this.toString()
                cache.put(this, result)
            }
            return result
        }

    private val String.long: Long
        get() {
            var result = cache.inverse().get(this)
            if (result == null) {
                result = this.toLong()
                cache.put(result, this)
            }
            return result
        }

    /* discovery callback for getting advertised devices */
    private val discoveryCallback = object : EndpointDiscoveryCallback() {

        override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
            val serviceId = info.serviceId.long
            if (this@Connection.serviceId != serviceId) {
                Timber.e(
                    "Unknown ServiceId (%d) from EndpointId (%s)",
                    serviceId,
                    info.endpointName
                )
                return
            }
            val peerId = info.endpointName.long
            Timber.v("Found EndpointId (%s) - PeerId (%d)", endpointId, peerId)
            //priority works: remove old endpoints if exists
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
            val peerId: Long = peerIdOf(endpointId)
            Timber.v("Endpoint lost (%s) - PeerId (%d)", endpointId, peerId)
            endpoints.remove(peerId)
            states.put(endpointId, State.LOST)
            directs.remove(endpointId)
            pendingEndpoints.remove(endpointId)
            requestTries.remove(endpointId)
        }
    }

    /* payload callback */
    private val payloadCallback = object : PayloadCallback() {

        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            val peerId = endpoints.inverseOf(endpointId)
            Timber.v("Payload Received from PeerId (%d)", peerId)
            executor.execute { callback.onPayload(peerId, payload) }
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
            val peerId = endpoints.inverseOf(endpointId)
            Timber.v("Payload Transfer Update from PeerId (%d)", peerId)
            executor.execute { callback.onPayloadStatus(peerId, update) }
        }

    }

    /* request thread */
    class RequestThread(val connection: Connection) : Runner() {

        @Throws(InterruptedException::class)
        override fun looping(): Boolean {
            val endpointId: String? = connection.pendingEndpoints.pollFirst()
            val peerId = endpointId?.let { connection.peerIdOf(it) } ?: 0L
            Timber.v("Next EndpointId (%s) - PeerId (%d)", endpointId, peerId)
            if (endpointId == null || peerId == 0L) {
                waitRunner(wait)
                wait += (delayS + delayS)
                return true
            }
            wait = delayS + delayS

            //already requested endpoint
            if (connection.states.containsKey(endpointId)) {
                Timber.v(
                    "States endpointId (%s) - PeerId (%d) - %s",
                    endpointId,
                    peerId,
                    connection.states.get(endpointId)
                )
            }
            if (connection.states.get(endpointId) == State.REQUESTING) {
                return true
            }

            //incoming endpoints: so don't make request on it
            if (connection.directs.valueOf(endpointId)) {
                return true
            }

            if (!times.containsKey(endpointId)) {
                times[endpointId] = currentMillis
            }

            if (!delays.containsKey(endpointId)) {
                delays[endpointId] = Utils.nextRand(5, 15) * delayS
            }

            if (!connection.requestTries.containsKey(endpointId)) {
                connection.requestTries.put(endpointId, 0)
            }

            val readyToRequest = times.timeOf(endpointId).isExpired(delays.valueOf(endpointId))
            Timber.v("Request Attempt %s %s", endpointId, readyToRequest)
            if (readyToRequest) {
                connection.requestConnection(endpointId)
                connection.states.put(endpointId, State.REQUESTING)
                times.remove(endpointId)
                delays.remove(endpointId)
                connection.requestTries.let {
                    it.put(endpointId, it.valueOf(endpointId).inc())
                }
            } else {
                connection.pendingEndpoints.insertLastUniquely(endpointId)
            }
            waitRunner(2 * delayS)
            return true
        }

    }


}