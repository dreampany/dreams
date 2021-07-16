package com.dreampany.network.nearby.core

import android.content.Context
import com.dreampany.network.misc.*
import com.google.android.gms.common.api.ApiException
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
        val peerId = info.endpointName
        Timber.v(
            "Connection Initiated [EndpointId-PeerId-Incoming]:[%s-%s-%s]",
            endpointId,
            peerId,
            info.isIncomingConnection
        )
        endpoints[peerId] = endpointId
        states[endpointId] = State.INITIATED
        directs[endpointId] = info.isIncomingConnection
        client.acceptConnection(endpointId, payloadCallback)
    }

    override fun onConnectionResult(endpointId: String, resolution: ConnectionResolution) {
        val accepted = resolution.status.statusCode == ConnectionsStatusCodes.STATUS_OK
        //TODO STATUS_CONNECTION_REJECTED

        val peerId = endpointId.peerId
        Timber.v(
            "Connection Result [EndpointId-PeerId-Accepted]:[%s-%s-%s]",
            endpointId,
            peerId,
            accepted
        )
        states[endpointId] = if (accepted) State.ACCEPTED else State.REJECTED

        if (accepted) {
            pendingEndpoints.remove(endpointId)
            if (peerId == null) {
                // TODO
            } else {
                executor.execute { callback.onConnection(peerId, true) }
            }
        } else {
            pendingEndpoints.insertLastUniquely(endpointId)
            executor.execute { startRequestThread() }
        }
    }

    override fun onDisconnected(endpointId: String) {
        Timber.v("Disconnected [Endpoint]:[%s]", endpointId)
        states[endpointId] = State.DISCONNECTED
        pendingEndpoints.insertLastUniquely(endpointId)
        val peerId = endpointId.peerId
        if (peerId == null) {
            //TODO
        } else {
            executor.execute {
                callback.onConnection(peerId, false)
                startRequestThread()
            }
        }
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

    fun stop() {
        synchronized(guard) {
            if (started.not()) return
            Timber.v("Stop Connection")
            started = false
            stopRequestThread()
            stopAdvertising()
            stopDiscovery()
        }
    }

    fun requireRestart(
        strategy: Strategy,
        serviceId: String,
        peerId: String
    ): Boolean = started
            && (this.strategy != strategy
            || this.serviceId != serviceId
            || this.peerId != peerId)

    fun send(peerId: String, payload: Payload): Boolean {
        val endpointId = peerId.endpointId
        if (endpointId == null) {
            Timber.v("Send Failed - PeerId (%s) EndpointId not found", peerId)
            return false
        }
        client.sendPayload(endpointId, payload)
        return true
    }

    /* private */
    private val String.peerId: String? get() = endpoints.inverseOf(this)

    private val String.endpointId: String?
        get() {
            val endpointId = endpoints.get(this) ?: return null
            //if (states.get(endpointId) != State.ACCEPTED) return null
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
            //if (!peerId.isValidPeerId) return

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

    /* payload callback */
    private val payloadCallback = object : PayloadCallback() {

        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            val peerId = endpointId.peerId ?: return
            //Timber.v("Payload Received from PeerId (%s)", peerId)
            executor.execute { callback.onPayload(peerId, payload) }
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
            val peerId = endpointId.peerId ?: return
            //Timber.v("Payload Transfer Update from PeerId (%s)", peerId)
            executor.execute { callback.onPayloadStatus(peerId, update) }
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
            if (::requestThread.isInitialized.not() || requestThread.running.not()) {
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
        Timber.v("Requesting Connection [EndpoindId-State]:[%s-%s]", endpointId, states[endpointId])
        val peerId = peerId ?: return

        client.requestConnection(peerId, endpointId, this)
            .addOnFailureListener {
                Timber.e(
                    it,
                    "Request Connection error [%s]-%s",
                    endpointId, it.message
                )

                if (it is ApiException) {
                    when (it.statusCode) {
                        ConnectionsStatusCodes.STATUS_ALREADY_CONNECTED_TO_ENDPOINT -> {
                            states[endpointId] = State.ALREADY_CONNECTED
                            pendingEndpoints.insertLastUniquely(endpointId)
                            startRequestThread()
                            return@addOnFailureListener
                        }
                        ConnectionsStatusCodes.STATUS_ENDPOINT_IO_ERROR,
                        ConnectionsStatusCodes.STATUS_RADIO_ERROR,
                        ConnectionsStatusCodes.STATUS_OUT_OF_ORDER_API_CALL -> {
                            states[endpointId] = State.ERROR
                            pendingEndpoints.insertLastUniquely(endpointId)
                            startRequestThread()
                            return@addOnFailureListener
                        }
                    }
                }

                states[endpointId] = State.REQUEST_FAILED
                pendingEndpoints.insertLastUniquely(endpointId)
                startRequestThread()
            }.addOnSuccessListener {
                Timber.v(
                    "Request Connection Success [EndpointId-PeerId]:[%s-%s]",
                    endpointId,
                    peerId
                )
                states[endpointId] = State.REQUEST_SUCCESS
                pendingEndpoints.remove(endpointId)
                requestTries.remove(endpointId)
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

                if (state == State.REQUESTING || state == State.ERROR) return true
                else if (state == State.ALREADY_CONNECTED) {
                    connection.executor.execute {
                        connection.callback.onConnection(peerId, true)
                    }
                    return true
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

            Timber.v("Request of EndpointId[%s] is %s", endpointId, readyToRequest)

            if (readyToRequest) {
                connection.requestConnection(endpointId)
                connection.states[endpointId] = State.REQUESTING
                times.remove(endpointId)
                delays.remove(endpointId)
                connection.requestTries.let {
                    it.put(endpointId, it.valueOf(endpointId).inc())
                }
            } else {
                connection.pendingEndpoints.insertLastUniquely(endpointId)
            }

            waitRunner(delayS)
            return true
        }

    }

}