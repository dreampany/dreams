package com.dreampany.network.nearby.core

import android.content.Context
import com.dreampany.network.misc.*
import com.dreampany.network.nearby.core.Packets.Companion.hash256
import com.dreampany.network.nearby.core.Packets.Companion.hash256AsLong
import com.dreampany.network.nearby.core.Packets.Companion.isHash
import com.dreampany.network.nearby.core.Packets.Companion.isMeta
import com.dreampany.network.nearby.core.Packets.Companion.isOkay
import com.dreampany.network.nearby.core.Packets.Companion.isPeer
import com.dreampany.network.nearby.core.Packets.Companion.peerHashPacket
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
import kotlin.collections.HashSet

/**
 * Created by roman on 7/9/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
open class NearbyApi(
    open var context: Context
) : Connection.Callback {

    enum class Type { PTP, CLUSTER, STAR }
    enum class PayloadState { SUCCESS, FAILURE, PROGRESS }

    interface Callback {
        fun onPeer(peer: Peer, state: Peer.State)
        fun onData(peer: Peer, data: ByteArray)
        fun onStatus(payloadId: Long, state: PayloadState, totalBytes: Long, bytesTransferred: Long)
    }

    protected lateinit var type: Type
    protected lateinit var serviceId: String
    protected lateinit var peerId: String
    protected var peerData: ByteArray? = null

    @Volatile
    protected var inited = false

    protected val guard = Object()

    protected val executor: Executor
    protected val callbacks: MutableSet<Callback>

    private val peers: MutableMap<String, Peer>
    private val states: MutableMap<String, Peer.State>
    private val synced: MutableMap<String, Boolean>

    private val timeouts: MutableMap<Id, MutablePair<Long, Long>>

    private val syncingPeers: SmartQueue<String>
    private val inputs: SmartQueue<MutablePair<String, Payload>>
    private val outputs: SmartQueue<MutablePair<String, Payload>>


    @Volatile
    private lateinit var syncingThread: Runner

    @Volatile
    private lateinit var outputThread: Runner

    @Volatile
    private lateinit var inputThread: Runner

    @Volatile
    private lateinit var connection: Connection

    init {
        executor = Executors.newCachedThreadPool()
        callbacks = Collections.synchronizedSet(HashSet<Callback>())

        peers = Maps.newConcurrentMap<String, Peer>()
        states = Maps.newConcurrentMap<String, Peer.State>()
        synced = Maps.newConcurrentMap<String, Boolean>()

        timeouts = Maps.newConcurrentMap<Id, MutablePair<Long, Long>>()

        syncingPeers = SmartQueue<String>()
        inputs = SmartQueue<MutablePair<String, Payload>>()
        outputs = SmartQueue<MutablePair<String, Payload>>()
    }

    override fun onConnection(peerId: String, connected: Boolean) {

    }

    override fun onPayload(peerId: String, payload: Payload) {
    }

    override fun onPayloadStatus(peerId: String, update: PayloadTransferUpdate) {
    }

    fun register(callback: Callback) {
        callbacks.add(callback)
    }

    fun unregister(callback: Callback) {
        callbacks.remove(callback)
    }

    protected open fun startApi(type: Type, serviceId: String, peerId: String) {
        check(inited) { "init() function need to be called before start()" }
        if (::connection.isInitialized.not()) {
            connection = Connection(context, executor, this)
        }
        if (connection.requireRestart(type.strategy, serviceId, peerId)) {
            stopApi()
        }
        connection.start(type.strategy, serviceId, peerId)
    }

    protected open fun stopApi() {
        check(inited) { "init() function need to be called before stop()" }
        stopSyncingThread()
        stopOutputThread()
        stopInputThread()
        if (::connection.isInitialized) {
            connection.stop()
        }
    }

    protected fun isLive(peer: Peer): Boolean = isLive(peer.id)

    protected fun isLive(peerId: String): Boolean = states.valueOf(peerId) == Peer.State.LIVE

    protected fun sendPacket(id: Id, packet: ByteArray, timeout: Long = 0) {
        val payload = Payload.fromBytes(packet)
        sendPayload(id, payload)
        resolveTimeout(id, timeout, 0L)
    }

    /* private */
    private val NearbyApi.Type.strategy: Strategy
        get() {
            return when (this) {
                Type.PTP -> Strategy.P2P_POINT_TO_POINT
                Type.CLUSTER -> Strategy.P2P_CLUSTER
                Type.STAR -> Strategy.P2P_STAR
                else -> Strategy.P2P_POINT_TO_POINT
            }
        }

    private fun sendPayload(id: Id, payload: Payload) {
        // payloads.put(payload.getId(), payload);
        // payloadIds.put(id, payload.getId());
        outputs.add(MutablePair.of(id.target, payload))
        startOutputThread()
    }

    private fun peerCallback(peer: Peer, state: Peer.State) {
        callbacks.forEach {
            it.onPeer(peer, state)
        }
    }

    private fun peerCallback(peer: Peer, data: ByteArray) {
        callbacks.forEach {
            it.onData(peer, data)
        }
    }

    private fun resolveTimeout(id: Id, timeout: Long, starting: Long) {
        if (!timeouts.containsKey(id)) {
            timeouts[id] = MutablePair.of(0L, 0L)
        }
        timeouts[id]?.let {
            it.left = timeout
            it.left = starting
        }
    }

    private fun resolvePacket(peerId: String, packet: ByteArray?) {
        if (packet == null) return
        Timber.v("Received: %d", packet.size)

        if (packet.isPeer) {
            Timber.v("Received peer packet: %d", packet.size)
            resolvePeerPacket(peerId, packet)
            return
        }
    }

    private fun resolvePeerPacket(peerId: String, packet: ByteArray) {
        if (packet.isHash) {
            Timber.v("Received peer hash packet (%d)", packet.size)
            val buffer = Packets.copyToBuffer(packet, 2)
            val remoteHash = buffer.long
            val ownHash = peerData.hash256AsLong
            Timber.v("Remote end own hash (%s) and Real own hash (%s)", remoteHash, ownHash)
            val id = Id(hash256, peerId, peerId)
            if (ownHash == remoteHash) {
                sendPacket(id, Packets.peerOkayPacket)
            } else {
                sendPacket(id, peerData.peerMetaPacket)
            }
            return
        }

        if (packet.isMeta) {
            Timber.v("Received meta packet (%d)", packet.size)
            val buffer = Packets.copyToBuffer(packet, 2)
            val peer = peers.get(peerId)
            peer?.let {
                it.meta = buffer.array()
                peerCallback(it, Peer.State.LIVE)
            }
            return
        }

        if (packet.isOkay) {
            Timber.v("Received okay packet (%d)", packet.size)
            val peer = peers.get(peerId)
            peer?.let {
                peerCallback(it, Peer.State.LIVE)
            }
        }
    }

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

    private fun startInputThread() {
        synchronized(guard) {
            if (::inputThread.isInitialized.not() || inputThread.running.not()) {
                inputThread = InputThread(this)
                inputThread.start()
            }
            inputThread.notifyRunner()
        }
    }

    private fun stopInputThread() {
        synchronized(guard) {
            if (::inputThread.isInitialized) {
                inputThread.stop()
            }
        }
    }

    private fun startOutputThread() {
        synchronized(guard) {
            if (::outputThread.isInitialized.not() || outputThread.running.not()) {
                outputThread = OutputThread(this)
                outputThread.start()
            }
            outputThread.notifyRunner()
        }
    }

    private fun stopOutputThread() {
        synchronized(guard) {
            if (::outputThread.isInitialized) {
                outputThread.stop()
            }
        }
    }

    class SyncingThread(val api: NearbyApi) : Runner() {

        private val timesOf: MutableMap<String, Long>

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
                    val remoteHash: Long = peer.meta.hash256AsLong
                    val packet = remoteHash.peerHashPacket
                    val id = Id(hash256, api.peerId, peer.id)
                    // send remote peer hash to remote end
                    api.sendPacket(id, packet)
                    timesOf.put(peerId, currentMillis)
                }
            }

            waitRunner(1000L)
            return true
        }
    }

    class InputThread(val api: NearbyApi) : Runner() {

        @Throws(InterruptedException::class)
        override fun looping(): Boolean {
            val input = api.inputs.pollFirst()
            if (input == null) {
                waitRunner(wait)
                wait += delayS
                return true
            }

            wait = delayS
            val peerId = input.left
            val payload = input.right

            when (payload.type) {
                Payload.Type.BYTES -> {
                    api.resolvePacket(peerId, payload.asBytes())
                }
            }

            waitRunner(delayMilli)
            return true
        }

    }


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

            if (api.isLive(peerId)) {
                val sending = api.connection.send(peerId, payload)
                Timber.v(
                    "Sending (%s) for PeerId (%s) PayloadId (%s)",
                    sending,
                    peerId,
                    payload.id
                )
            }
            waitRunner(delayMilli)
            return true
        }

    }

}