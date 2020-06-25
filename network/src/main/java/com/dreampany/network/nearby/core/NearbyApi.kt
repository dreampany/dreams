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
        fun onData(peer: Peer, meta: ByteArray)
        fun onStatus(payloadId: Long, state: PayloadState, totalBytes: Long, bytesTransferred: Long)
    }

    protected lateinit var strategy: Strategy
    protected lateinit var serviceId: String
    protected lateinit var peerId: String
    protected var peerData: ByteArray? = null

    protected val guard = Object()

    @Volatile
    protected var inited = false

    protected val executor: Executor
    protected val callbacks: MutableSet<Callback>

    @Volatile
    private lateinit var connection: Connection

    private val peers: MutableMap<String, Peer>
    private val states: MutableMap<String, Peer.State>
    private val synced: MutableMap<String, Boolean>

    private val timeouts: MutableMap<Id, MutablePair<Long, Long>>

    private val syncingPeers: SmartQueue<String>
    private val outputs: SmartQueue<MutablePair<String, Payload>>
    private val inputs: SmartQueue<MutablePair<String, Payload>>


    @Volatile
    private lateinit var syncingThread: Runner

    @Volatile
    private lateinit var outputThread: Runner

    @Volatile
    private lateinit var inputThread: Runner

    init {

        executor = Executors.newCachedThreadPool()
        callbacks = Collections.synchronizedSet(HashSet<Callback>())

        peers = Maps.newConcurrentMap<String, Peer>()
        states = Maps.newConcurrentMap<String, Peer.State>()
        synced = Maps.newConcurrentMap<String, Boolean>()

        timeouts = Maps.newConcurrentMap<Id, MutablePair<Long, Long>>()

        syncingPeers = SmartQueue<String>()
        outputs = SmartQueue<MutablePair<String, Payload>>()
        inputs = SmartQueue<MutablePair<String, Payload>>()
    }

    override fun onConnection(peerId: String, connected: Boolean) {
        Timber.v("Peer (%s) - Connection - (%s)", peerId, connected)
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

    override fun onPayload(peerId: String, payload: Payload) {
        Timber.v("Payload Received from: %s", peerId)
        inputs.add(MutablePair.of(peerId, payload))
        startInputThread()
    }

    override fun onPayloadStatus(peerId: String, update: PayloadTransferUpdate) {
        val pair = outputs.find { it.right.id == update.payloadId }
        if (pair != null) {
            outputs.remove(pair)
            Timber.v("Removed PeerId (%s) PayloadId (%s)", peerId, update.payloadId)
        }
    }

    fun register(callback: Callback) {
        callbacks.add(callback)
    }

    fun unregister(callback: Callback) {
        callbacks.remove(callback)
    }

    protected open fun startApi(strategy: Strategy, serviceId: String, peerId: String) {
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

    protected fun isLive(peer: Peer): Boolean = isLive(peer.id)

    protected fun isLive(peerId: String): Boolean = states.valueOf(peerId) == Peer.State.LIVE

    protected fun sendPacket(id: Id, packet: ByteArray, timeout: Long = 0) {
        val payload = Payload.fromBytes(packet)
        sendPayload(id, payload)
        resolveTimeout(id, timeout, 0L)
    }

    fun sendPayload(id: Id, payload: Payload) {
        // payloads.put(payload.getId(), payload);
        // payloadIds.put(id, payload.getId());
        outputs.add(MutablePair.of(id.target, payload))
        startOutputThread()
    }

    private fun peerCallback(peer: Peer, state : Peer.State) {
        callbacks.forEach {
            it.onPeer(peer, state)
        }
    }

    private fun peerCallback(peer: Peer, meta : ByteArray) {
        callbacks.forEach {
            it.onData(peer, meta)
        }
    }

    private fun resolveTimeout(id: Id, timeout: Long, starting: Long) {
        if (!timeouts.containsKey(id)) {
            timeouts.put(id, MutablePair.of(0L, 0L))
        }
        timeouts[id]?.left = timeout
        timeouts[id]?.right = starting
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
                    val id = Id(hash256, api.connection.getPeerId(), peer.id)
                    // send remote peer hash to remote end
                    api.sendPacket(id, packet)
                    timesOf.put(peerId, currentMillis)
                }
            }

            waitRunner(1000L)
            return true
        }

    }

    /* output thread */
    private fun startOutputThread() {
        synchronized(guard) {
            if (!::outputThread.isInitialized || !outputThread.running) {
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
                Timber.v("Sending (%s) for PeerId (%s) PayloadId (%s)", sending, peerId, payload.id)
            }
            waitRunner(100L)
            return true
        }
    }

    /* input thread */
    private fun startInputThread() {
        synchronized(guard) {
            if (!::inputThread.isInitialized || !inputThread.running) {
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

            waitRunner(100L)
            return true
        }
    }
}