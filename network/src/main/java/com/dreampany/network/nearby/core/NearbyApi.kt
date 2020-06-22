package com.dreampany.network.nearby.core

import android.content.Context
import com.dreampany.network.misc.*
import com.dreampany.network.nearby.core.Packets.Companion.hash256
import com.dreampany.network.nearby.core.Packets.Companion.peerMetaPacket
import com.dreampany.network.nearby.model.Id
import com.dreampany.network.nearby.model.Peer
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import com.google.android.gms.nearby.connection.Strategy
import com.google.common.collect.Maps
import org.apache.commons.lang3.tuple.MutablePair
import timber.log.Timber
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Created by roman on 20/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
open class NearbyApi(
    val context: Context
) : Connection.Callback {

    enum class PayloadState { SUCCESS, FAILURE, PROGRESS }

    interface Callback {
        fun onPeer(peer: Peer, state: Peer.State)
        fun onData(peer: Peer, data: ByteArray)
        fun onStatus(payloadId: Long, state: PayloadState, totalBytes: Long, bytesTransferred: Long)
    }

    protected val guard = Object()

    @Volatile
    protected var inited = false

    protected val executor: Executor
    protected val callbacks: MutableSet<Callback>

    @Volatile
    private lateinit var connection: Connection

    private val peers: MutableMap<Long, Peer>
    private val states: MutableMap<Long, Peer.State>
    private val synced: MutableMap<Long, Boolean>

    private val timeouts: MutableMap<Id, MutablePair<Long, Long>>

    private val syncingPeers: SmartQueue<Long>
    private val outputs: SmartQueue<MutablePair<Long, Payload>>
    private val inputs: SmartQueue<MutablePair<Long, Payload>>


    @Volatile
    private lateinit var syncingThread: Runner
    init {

        executor = Executors.newCachedThreadPool()
        callbacks = Collections.synchronizedSet(HashSet<Callback>())

        peers = Maps.newConcurrentMap<Long, Peer>()
        states = Maps.newConcurrentMap<Long, Peer.State>()
        synced = Maps.newConcurrentMap<Long, Boolean>()

        timeouts = Maps.newConcurrentMap<Id, MutablePair<Long, Long>>()

        syncingPeers = SmartQueue<Long>()
        outputs = SmartQueue<MutablePair<Long, Payload>>()
        inputs = SmartQueue<MutablePair<Long, Payload>>()
    }

    override fun onConnection(peerId: Long, connected: Boolean) {
        Timber.v("Peer (%d) - Connection - (%s)", peerId, connected)
        if (!peers.containsKey(peerId)) {
            val peer = Peer(peerId)
            peers[peerId] = peer
        }
        states[peerId] = if (connected) Peer.State.LIVE else Peer.State.DEAD
        if (connected) {
            syncingPeers.insertLastUniquely(peerId)
            startSyncingThread()
        } else {
            syncingPeers.remove(peerId)
        }
    }

    override fun onPayload(peerId: Long, payload: Payload) {
        Timber.v("Payload Received from: %d", peerId)
        inputs.add(MutablePair.of(peerId, payload))
        //startInputThread()
    }

    override fun onPayloadStatus(peerId: Long, status: PayloadTransferUpdate) {
    }

    fun register(callback: NearbyApi.Callback) {
        callbacks.add(callback)
    }

    fun unregister(callback: NearbyApi.Callback) {
        callbacks.remove(callback)
    }

    protected open fun startApi(strategy: Strategy, serviceId: Long, peerId: Long) {
        check(inited) { "init() function need to be called before start()" }
        if (::connection.isInitialized) {
            if (connection.requireRestart(strategy, serviceId, peerId).value) {
                stopApi()
                executor.execute {
                    connection = Connection(context, executor, strategy, serviceId, peerId, this)
                    connection.start()
                }
            }
        } else {
            executor.execute {
                connection = Connection(context, executor, strategy, serviceId, peerId, this)
                connection.start()
            }
        }
    }

    protected open fun stopApi() {
        check(inited) { "init() function need to be called before stop()" }
        if (::connection.isInitialized) {
            connection.stop()
        }
    }

    protected fun isLive(peer: Peer): Boolean = states.valueOf(peer.id) == Peer.State.LIVE

    protected fun sendPacket(id: Id, packet: ByteArray, timeout: Long) {
        val payload = Payload.fromBytes(packet)
        sendPayload(id, payload)
        resolveTimeout(id, timeout, 0L)
    }

    fun sendPayload(id: Id, payload: Payload) {
        // payloads.put(payload.getId(), payload);
        // payloadIds.put(id, payload.getId());
        outputs.add(MutablePair.of(id.target, payload))
        //startOutputThread()
    }

    open fun resolveTimeout(id: Id, timeout: Long, starting: Long) {
        if (!timeouts.containsKey(id)) {
            timeouts.put(id, MutablePair.of(0L, 0L))
        }
        timeouts[id]?.left = timeout
        timeouts[id]?.right = starting
    }

/*    private fun sendPayload(peerId: Long, data: ByteArray, timeoutInMs: Long) {
        synchronized(guard) {
            *//* val packet: ByteArray = Packets.getDataPacket(data)
             super.sendPacket(id, packet, timeoutInMs)*//*
        }
    }*/

    /* syncing thread */
    private fun startSyncingThread() {
        synchronized(guard) {
            if (!::syncingThread.isInitialized || !syncingThread.running) {
                syncingThread = SyncingThread(this)
                syncingThread.start()
            }
            syncingThread.notifyRunner()
        }
    }

    private fun stopSyncingThread() {
        synchronized(guard) {
            if (::syncingThread.isInitialized) {
                syncingThread.stop()
            }
        }
    }

    class SyncingThread(val api: NearbyApi) : Runner() {

        private val timesOf: MutableMap<Long, Long>

        init {
            timesOf = Maps.newConcurrentMap()
        }

        @Throws(InterruptedException::class)
        override fun looping(): Boolean {
            val peerId = api.syncingPeers.pollFirst()
            if (peerId == null) {
                waitRunner(wait)
                wait += delayS
                return true
            }
            wait = delayS

            if (api.synced.valueOf(peerId)) {
                return true
            }
            if (timesOf.valueOf(peerId).isExpired(5 * delayS)) {
                val peer = api.peers.get(peerId)
                if (peer != null) {
                    Timber.v("Next syncing peer (%s)", peer.id)

                    val hash: Long = peer.meta.hash256
                    val packet = hash.peerMetaPacket

                    val id = Id(hash256, api.connection.getPeerId(), peer.id)
                    api.sendPacket(id, packet, 0L)
                    timesOf.put(peerId, currentMillis)
                }
            }

            waitRunner(1000L)
            return true
        }

    }

    /* output thread */
    class OutputThread(val api: NearbyApi) : Runner() {

        @Throws(InterruptedException::class)
        override fun looping(): Boolean {
            val output = api.outputs.pollFirst()
            if (output == null) {
                waitRunner(wait)
                wait += delayS
                return true
            }
            wait = delayS
            val peerId = output.left
            val payload = output.right
            return true
        }

    }
}