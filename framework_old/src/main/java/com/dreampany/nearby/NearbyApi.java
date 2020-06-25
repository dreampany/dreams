package com.dreampany.nearby;

import android.Manifest;
import android.content.Context;
import androidx.annotation.NonNull;

import com.dreampany.nearby.core.Api;
import com.dreampany.nearby.core.Packets;
import com.dreampany.nearby.model.Id;
import com.dreampany.nearby.model.Meta;
import com.dreampany.nearby.model.Peer;
import com.dreampany.nearby.util.GooglePlayUtil;
import com.dreampany.nearby.util.NearbyUtil;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Hawladar Roman on 6/4/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class NearbyApi extends Api {

    public enum PayloadState {
        SUCCESS, FAILURE, PROGRESS
    }

    public interface Processor {
        void putMeta(Id id, Meta meta);

        void putFile(Id id, File file);

        Meta getMeta(Id id);

        File getFile(Id id);
    }

    public interface Callback {Callback
        void onPeer(Peer peer, Peer.State state);

        void onData(Peer peer, byte[] data);

        void onStatus(long payloadId, PayloadState state, long totalBytes, long bytesTransferred);
    }

    @Inject
    NearbyApi() {
        super();
    }

    public boolean init(Context context, long serviceId, long peerId) {
        return init(context, serviceId, peerId, null);
    }

    public boolean init(Context context, long serviceId, long peerId, byte[] peerData) {
        synchronized (guard) {
            if (inited) {
                return true;
            }

            if (context == null) {
                throw new IllegalArgumentException("Context must be non-null");
            }

            if (serviceId == 0L) {
                throw new IllegalArgumentException("ServiceId must be non-zero");
            }

            if (peerId == 0L) {
                throw new IllegalArgumentException("PeerId must be non-zero");
            }

            if (this.context == null) {
                this.context = context.getApplicationContext();
            }

            //check some permission and google api
            if (!GooglePlayUtil.isPlayAvailable(this.context)) {
                throw new IllegalStateException("Google Play service is not available!");
            }

            if (!NearbyUtil.hasPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                throw new IllegalStateException("Location permission is missing!");
            }

            this.serviceId = serviceId;
            peer = new Peer(peerId, peerData);
            inited = true;
        }
        return inited;
    }

    public void setPeerData(byte[] peerData) {
        peer.setData(peerData);
        //TODO notify to remote end that own data has been changed
    }

    public void register(@NonNull  callback) {
        callbacks.add(callback);
    }

    public void unregister(@NonNull Callback callback) {
        callbacks.remove(callback);
    }

/*    public void observePeer(LifecycleOwner owner, Observer<Peer> observer) {
        peerOwner = owner;
        peerOutput.removeObserver(observer);
        peerOutput.observe(owner, observer);
    }*/

    public void start() {
        synchronized (guard) {
            super.start();
        }
    }

    public void stop() {
        synchronized (guard) {
            super.stop();
        }
    }

    /**
     * @param id
     * @param data
     * @param timeoutInMs timeout in millis
     */
    public void send(Id id, byte[] data, long timeoutInMs) {
        synchronized (guard) {
            byte[] packet = Packets.getDataPacket(data);
            super.sendPacket(id, packet, timeoutInMs);
        }
    }

    public void send(Id id, File file, long timeout) {
        synchronized (guard) {
            super.sendFile(id, file, timeout);
        }
    }
}
