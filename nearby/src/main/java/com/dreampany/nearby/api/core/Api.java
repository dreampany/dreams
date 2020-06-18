package com.dreampany.nearby.api.core;

import android.content.Context;


import com.dreampany.nearby.api.NearbyApi;
import com.dreampany.nearby.api.misc.Runner;
import com.dreampany.nearby.api.misc.SmartQueue;
import com.dreampany.nearby.api.model.Id;
import com.dreampany.nearby.api.model.Peer;
import com.dreampany.nearby.api.util.NearbyUtil;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.common.collect.Maps;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.MutableTriple;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import timber.log.Timber;

/**
 * Created by Hawladar Roman on 6/4/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class Api implements Connection.Callback {
    protected static final Object guard = new Object();
    protected volatile boolean inited;
    protected Executor executor;
    protected NearbyApi.Processor processor;
    protected Set<NearbyApi.Callback> callbacks;
    protected Context context;
    protected long serviceId;
    protected Peer peer;

    private volatile Connection connection;
    protected Map<Long, Peer> peers; // peerId to Peer
    protected Map<Long, Peer.State> states; // peerId to Boolean
    Map<Long, Long> liveTimes;
    Map<Long, Boolean> synced; // peerId to Boolean

    Map<String, Long> endpoints; // endpoint to peerId
    Map<Long, Payload> payloads; // keep only file and stream payload;
    Map<Id, Long> payloadIds;    // keep only file and stream payload;

    Map<Id, MutablePair<Long, Long>> timeouts; // key to (timeout, startingOfTransfer)

    SmartQueue<Long> syncingPeers;
    SmartQueue<MutablePair<Long, Payload>> outputs; // peerId and payload # peerId to resolve endpoint dynamic way
    SmartQueue<MutablePair<Long, Payload>> inputs; // endpoint and payload # endpoint to resolve peerId dynamic way

    Runner syncingThread, outputThread, inputThread, downloadThread;

    protected Api() {
        executor = Executors.newCachedThreadPool();
        callbacks = Collections.synchronizedSet(new HashSet<NearbyApi.Callback>());

        peers = Maps.newConcurrentMap();
        states = Maps.newConcurrentMap();
        liveTimes = Maps.newConcurrentMap();
        synced = Maps.newConcurrentMap();

        endpoints = Maps.newConcurrentMap();
        payloads = Maps.newConcurrentMap();
        payloadIds = Maps.newConcurrentMap();

        syncingPeers = new SmartQueue<>();
        outputs = new SmartQueue<>();
        inputs = new SmartQueue<>();

        timeouts = Maps.newConcurrentMap();
    }

    protected void start() {
        if (!inited) {
            throw new IllegalStateException("init() function need to be called before start()");
        }
        if (connection == null) {
            connection = new Connection(this.context, executor, serviceId, peer.getId(), Strategy.P2P_CLUSTER, this);
        }
        connection.start();
    }

    protected void stop() {
        if (!inited) {
            return;
        }
        stopInputThread();
        stopOutputThread();
        stopSyncingThread();
        if (connection != null) {
            connection.stop();
        }
    }
    @Override
    public void onConnection(long peerId, boolean connected) {
        Timber.v("Peer (%d) - Connection - (%s)", peerId, connected);
        if (!peers.containsKey(peerId)) {
            Peer peer = new Peer(peerId);
            peers.put(peerId, peer);
        }
        states.put(peerId, connected ? Peer.State.LIVE : Peer.State.DEAD);
        if (connected) {
            syncingPeers.insertLastUniquely(peerId);
            startSyncingThread();
        } else {
            syncingPeers.remove(peerId);
        }
    }

    @Override
    public void onPayload(long peerId, Payload payload) {
        Timber.v("Payload Received from: %d", peerId);
        resolvePayload(peerId, payload);
    }

    @Override
    public void onPayloadStatus(long peerId, PayloadTransferUpdate status) {

    }

    protected boolean isLive(Peer peer) {
        return states.containsKey(peer.getId()) && states.get(peer.getId()) == Peer.State.LIVE;
    }

    protected void sendPacket(Id id, byte[] packet, long timeout) {
        Payload payload = Payload.fromBytes(packet);
        sendPayload(id, payload);
        resolveTimeout(id, timeout, 0L);
    }

    protected void sendFile(Id id, File file, long timeout) {
        try {
            Payload payload = Payload.fromFile(file);
            byte[] filePayloadId = Packets.getFilePayloadIdPacket(id.id, payload.getId());
            sendPacket(id, filePayloadId, 0L);
            sendPayload(id, payload);
            resolveTimeout(id, timeout, 0L);
        } catch (FileNotFoundException e) {
            Timber.e("error: %s", e.toString());
        }
    }

    void sendPayload(Id id, Payload payload) {
        // payloads.put(payload.getId(), payload);
        // payloadIds.put(id, payload.getId());
        outputs.add(MutablePair.of(id.target, payload));
        startOutputThread();
    }

    void resolveTimeout(Id id, long timeout, long starting) {
        if (!timeouts.containsKey(id)) {
            timeouts.put(id, MutablePair.of(0L, 0L));
        }
        timeouts.get(id).setLeft(timeout);
        timeouts.get(id).setRight(starting);
    }

    void resolvePayload(long peerId, Payload payload) {
        inputs.add(MutablePair.of(peerId, payload));
        startInputThread();
    }

    private void resolvePacket(long peerId, byte[] packet) {

        Timber.v("Received: %d", packet.length);

        if (Packets.isPeer(packet)) {
            Timber.v("Received peer packet: %d", packet.length);
            resolvePeerPacket(peerId, packet);
            return;
        }

        if (Packets.isData(packet)) {
            byte[] bytes = NearbyUtil.copy(packet, 1);
            resolveDataPacket(peerId, bytes);
            return;
        }

        if (Packets.isFile(packet)) {
            // byte[] filePacket = DataUtil.copy(packet, 1);
            resolveFilePacket(peerId, packet);
            return;
        }
    }

    private void resolvePeerPacket(long peerId, byte[] packet) {
        if (Packets.isMeta(packet)) {
            Timber.v("Received meta packet (%d)", packet.length);
            ByteBuffer meta = NearbyUtil.copyToBuffer(packet, 2);
            long peerHash = meta.getLong();

            long ownHash = NearbyUtil.getSha256(this.peer.getData());

            Timber.v("Remote end own hash (%s) and Real own hash (%s)", peerHash, ownHash);

            if (peerHash == ownHash) {
                byte[] okay = Packets.getPeerOkayPacket();
                Id id = new Id();
                id.source = peer.getId();
                id.target = peerId;
                sendPacket(id, okay, 0L);
            } else {
                byte[] peerData = Packets.getPeerDataPacket(this.peer.getData());
                Timber.v("Send peer data (%d)", peerData.length);
                Id id = new Id();
                id.source = peer.getId();
                id.target = peerId;
                sendPacket(id, peerData, 0L);
            }

            return;
        }

        if (Packets.isOkay(packet)) {
            Timber.v("Received okay packet (%d)", packet.length);
            Peer peer = peers.get(peerId);
            peerCallback(peer, Peer.State.LIVE);
            return;
        }

        if (Packets.isPeerData(packet)) {
            Timber.v("Received data packet (%d)", packet.length);
            ByteBuffer buf = NearbyUtil.copyToBuffer(packet, 2);
            Peer peer = peers.get(peerId);
            peer.setData(buf.array());

            Timber.v("Received peer data (%d)", peer.getData().length);

            peers.get(peerId);
            peerCallback(peer, Peer.State.LIVE);
            return;
        }
    }

    private void resolveDataPacket(long peerId, byte[] data) {
        Peer peer = peers.get(peerId);
        peerCallback(peer, data);
    }

    private void resolveFilePacket(long peerId, byte[] packet) {
       /* if (Packets.isId(packet)) {
            ByteBuffer idBuffer = DataUtil.copyToBuffer(packet, 2);
            long fileId = idBuffer.getLong();
            long source = idBuffer.getLong();
            Id id = new Id(fileId, source);
           // keepDownloads(peerId, fileId);
            return;
        }*/

        /*if (Packets.isMeta(packet)) {
            ByteBuffer metaBuffer = DataUtil.copyToBuffer(packet, 2);
            long fileId = metaBuffer.getLong();
            byte[] meta = new byte[metaBuffer.remaining()];
            metaBuffer.get(meta);
           // keepDownloads(peerId, fileId, meta);
            return;
        }*/

/*        if (Packets.isFilePayloadId(packet)) {
            ByteBuffer metaBuffer = DataUtil.copyToBuffer(packet, 2);
            long fileId = metaBuffer.getLong();
            long payloadId = metaBuffer.getLong();
           // downloadPayloadIds.put(fileId, payloadId);
            return;
        }*/

/*        if (Packets.isMetaRequest(packet)) {
            ByteBuffer metaBuffer = DataUtil.copyToBuffer(packet, 1);
            long fileId = metaBuffer.getLong();
            Meta meta = processor.getMeta(fileId);
            byte[] metaPacket = Packets.getFileMetaPacket(fileId, meta);
            sendPacket(peerId, 0L, metaPacket);
            return;
        }*/

        /*if (Packets.isDataRequest(packet)) {
            ByteBuffer dataBuffer = DataUtil.copyToBuffer(packet, 1);
            long fileId = dataBuffer.getLong();
            File file = processor.getFile(fileId);
            try {
                Payload payload = Payload.fromFile(file);
                byte[] filePayloadId = Packets.getFilePayloadIdPacket(fileId, payload.getId());
                sendPacket(peerId, 0L, filePayloadId);
                sendPayload(peerId, 0L, payload);
            } catch (FileNotFoundException e) {
            }
            return;
        }*/
    }

    private void peerCallback(Peer peer, Peer.State state) {
        for (NearbyApi.Callback callback : callbacks) {
            callback.onPeer(peer, state);
        }
    }

    private void peerCallback(Peer peer, byte[] data) {
        for (NearbyApi.Callback callback : callbacks) {
            callback.onData(peer, data);
        }
    }

    private void statusCallback(long payloadId, NearbyApi.PayloadState status, long totalBytes, long bytesTransferred) {
        for (NearbyApi.Callback callback : callbacks) {
            callback.onStatus(payloadId, status, totalBytes, bytesTransferred);
        }
    }

    //thread management

    void startOutputThread() {
        if (outputThread == null || !outputThread.isRunning()) {
            outputThread = new OutputThread();
            outputThread.start();
        }
        outputThread.notifyRunner();
    }

    void stopOutputThread() {
        if (outputThread != null) {
            outputThread.stop();
        }
    }

    void startInputThread() {
        if (inputThread == null || !inputThread.isRunning()) {
            inputThread = new InputThread();
            inputThread.start();
        }
        inputThread.notifyRunner();
    }

    void stopInputThread() {
        if (inputThread != null) {
            inputThread.stop();
        }
    }

    void startDownloadThread() {
        if (downloadThread == null || !downloadThread.isRunning()) {
            downloadThread = new DownloadThread();
            downloadThread.start();
        }
        downloadThread.notifyRunner();
    }

    private void startSyncingThread() {
        if (syncingThread == null || !syncingThread.isRunning()) {
            syncingThread = new SyncingThread();
            syncingThread.start();
        }
        syncingThread.notifyRunner();
    }

    private void stopSyncingThread() {
        if (syncingThread != null) {
            syncingThread.stop();
        }
    }

    private class SyncingThread extends Runner {

        long delay;
        Map<Long, Long> times; //peerId to time

        SyncingThread() {
            times = Maps.newHashMap();
            delay = 20 * delayS;
        }

        @Override
        protected boolean looping() throws InterruptedException {

            Long peerId = syncingPeers.pollFirst();

            if (peerId == null) {
                waitRunner(wait);
                wait += delayS;
                return true;
            }
            wait = delayS;

            if (synced.containsKey(peerId) && synced.get(peerId)) {
                return true;
            }

            if (!times.containsKey(peerId)) {
                times.put(peerId, 0L);
            }

            if (isExpired(times.get(peerId), 5 * delayS)) {
                Peer peer = peers.get(peerId);
                Timber.v("Next syncing peer (%d)", peer.getId());

                long metaHash = NearbyUtil.getSha256(peer.getData());
                byte[] metaPacket = Packets.getPeerMetaPacket(metaHash);

                Id id = new Id();
                id.source = Api.this.peer.getId();
                id.target = peer.getId();
                sendPacket(id, metaPacket, 0L);
                times.put(peerId, System.currentTimeMillis());
            }

            waitRunner(10L);
            return true;
        }
    }

    private class OutputThread extends Runner {

        OutputThread() {
        }

        @Override
        protected boolean looping() throws InterruptedException {

            MutablePair<Long, Payload> output = outputs.pollFirst();

            if (output == null) {
                waitRunner(wait);
                wait += delayS;
                return true;
            }

            wait = delayS;

            long peerId = output.getLeft();
            Payload payload = output.getRight();
            boolean sent = connection.send(peerId, payload);

            //MutablePair<MutableTriple<Long, Long, Payload>, Long> pendingItem = MutablePair.of(output, System.currentTimeMillis());
            //   outgoing.put(payload.getId(), pendingItem);

            waitRunner(10L);
            return true;
        }
    }

    private class InputThread extends Runner {

        InputThread() {
        }

        @Override
        protected boolean looping() throws InterruptedException {

            MutablePair<Long, Payload> input = inputs.takeFirst();

            if (input == null) {
                waitRunner(wait);
                wait += delayS;
                return true;
            }
            wait = delayS;

            long peerId = input.getLeft();
            Payload payload = input.getRight();

            //TODO process the input payload

            switch (payload.getType()) {
                case Payload.Type.BYTES:
                    resolvePacket(peerId, payload.asBytes());
                    break;
            }
            waitRunner(10L);
            return true;
        }
    }

    private class DownloadThread extends Runner {

        long downloadDelay = 60 * delayS;

        DownloadThread() {
        }

        Map.Entry<Long, MutableTriple<Long, byte[], File>> nextDownload() {
/*            for (Map.Entry<Long, MutableTriple<Long, byte[], File>> entry : downloads.entrySet()) {
                long fileId = entry.getKey();
                if (isExpired(fileId, downloadDelay)) {
                    return entry;
                }
            }*/
            return null;
        }


        @Override
        protected boolean looping() throws InterruptedException {

            Map.Entry<Long, MutableTriple<Long, byte[], File>> item = nextDownload();

            if (item == null) {
                waitRunner(wait);
                wait += delayS;
                return true;
            }

            wait = delayS;

            long fileId = item.getKey();
            long peerId = item.getValue().getLeft();
            byte[] fileMeta = item.getValue().getMiddle();
            File file = item.getValue().getRight();

            /*if (fileMeta == null || fileMeta.length == 0) {
                byte[] metaRequest = Packets.getFileMetaRequestPacket(fileId);
                sendPacket(peerId, 0L, metaRequest);
            } else if (file == null) {
                byte[] fileRequest = Packets.getFileDataRequestPacket(fileId);
                sendPacket(peerId, 0L, fileRequest);
            }*/

            waitRunner(10L);
            return false;
        }
    }
}
