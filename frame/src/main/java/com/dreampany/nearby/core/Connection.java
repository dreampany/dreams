package com.dreampany.nearby.core;

import android.content.Context;

import androidx.annotation.NonNull;

import com.dreampany.nearby.misc.Runner;
import com.dreampany.nearby.misc.SmartQueue;
import com.dreampany.nearby.util.NearbyUtil;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.common.base.Strings;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

/**
 * Created by Hawladar Roman on 6/4/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
class Connection extends ConnectionLifecycleCallback {

    private final Object guard = new Object();

    private enum State {
        FOUND, LOST,
        REQUESTING, REQUEST_SUCCESS, REQUEST_FAILED,
        INITIATED, ACCEPTED, REJECTED, DISCONNECTED
    }

    interface Callback {
        void onConnection(long peerId, boolean connected);

        void onPayload(long peerId, Payload payload);

        void onPayloadStatus(long peerId, PayloadTransferUpdate status);
    }

    private Context context;
    private Executor executor;
    private long serviceId;
    private long peerId;
    private Callback callback;
    private ConnectionsClient client;
    private AdvertisingOptions advertisingOptions;
    private DiscoveryOptions discoveryOptions;

    private volatile boolean advertising = false;
    private volatile boolean discovering = false;

    private BiMap<Long, String> cache;
    private BiMap<Long, String> endpoints; // peerId to endpointId
    private Map<String, State> states;     // endpointId to State
    private Map<String, Boolean> directs;  // endpointId to Directs (incoming = true or outgoing = false)
    private SmartQueue<String> pendingEndpoints;
    private Map<String, Integer> requestTries;
    private final int MAX_TRY = 5;

    private volatile RequestThread requestThread;

    Connection(Context context, Executor executor, long serviceId, long peerId, Strategy strategy, Callback callback) {
        this.context = context.getApplicationContext();
        this.executor = executor;
        this.serviceId = serviceId;
        this.peerId = peerId;
        this.callback = callback;
        this.client = Nearby.getConnectionsClient(this.context);
        this.advertisingOptions = new AdvertisingOptions.Builder().setStrategy(strategy).build();
        this.discoveryOptions = new DiscoveryOptions.Builder().setStrategy(strategy).build();

        cache = HashBiMap.create();
        endpoints = HashBiMap.create();
        states = Maps.newConcurrentMap();
        directs = Maps.newConcurrentMap();
        pendingEndpoints = new SmartQueue<>();
        requestTries = Maps.newConcurrentMap();
    }

    @Override
    public void onConnectionInitiated(@NonNull String endpointId, @NonNull ConnectionInfo info) {
        long peerId = toLong(info.getEndpointName());
        endpoints.put(peerId, endpointId);
        states.put(endpointId, State.INITIATED);
        directs.put(endpointId, info.isIncomingConnection());
        client.acceptConnection(endpointId, payloadCallback);
        Timber.v("Connection initiated for EndpointId(%s) Peer(%d) incoming %s", endpointId, peerId, info.isIncomingConnection());
    }

    @Override
    public void onConnectionResult(@NonNull String endpointId, @NonNull ConnectionResolution result) {

        boolean accepted = result.getStatus().getStatusCode() == ConnectionsStatusCodes.STATUS_OK;

        //TODO STATUS_CONNECTION_REJECTED

        Timber.v("Connection Result endpoint: %s accepted %s", endpointId, accepted);
        states.put(endpointId, accepted ? State.ACCEPTED : State.REJECTED);

        if (accepted) {
            pendingEndpoints.remove(endpointId);
            long peerId = endpoints.inverse().get(endpointId);
            if (callback != null) {
                callback.onConnection(peerId, true);
            }
        } else {
            pendingEndpoints.insertLastUniquely(endpointId);
            startRequestThread();
        }
    }

    @Override
    public void onDisconnected(@NonNull String endpointId) {
        Timber.v("Disconnected endpoint: %s", endpointId);
        states.put(endpointId, State.DISCONNECTED);
        pendingEndpoints.remove(endpointId);
        long peerId = getPeerId(endpointId);
        executor.execute(() -> {
            if (callback != null)
                callback.onConnection(peerId, false);
        });
        startRequestThread();
    }

    void start() {
        synchronized (guard) {
            startAdvertising();
            startDiscovery();
        }
    }

    void stop() {
        synchronized (guard) {
            stopRequestThread();
            stopAdvertising();
            stopDiscovery();
        }
    }

    private void startAdvertising() {
        synchronized (guard) {
            if (advertising) {
                return;
            }
            client.startAdvertising(
                    toString(peerId),
                    toString(serviceId),
                    this,
                    advertisingOptions)
                    .addOnSuccessListener(ignored -> {
                        advertising = true;
                        Timber.v("Success Advertising of Peer (%d) - ServiceId (%d)", peerId, serviceId);
                    })
                    .addOnFailureListener(error -> {
                        advertising = false;
                        Timber.e("Error in Advertising of Peer (%d) - ServiceId (%d) - %s", peerId, serviceId, error.getMessage());
                    });
        }
    }

    private void startDiscovery() {
        synchronized (guard) {
            if (discovering) {
                return;
            }
            client.startDiscovery(
                    toString(serviceId),
                    discoveryCallback,
                    discoveryOptions)
                    .addOnSuccessListener(ignored -> {
                        discovering = true;
                        Timber.v("Success Discovering Peer (%d) - ServiceId (%d)", peerId, serviceId);
                    })
                    .addOnFailureListener(error -> {
                        discovering = false;
                        Timber.e("Error Discovering Peer (%d) - ServiceId (%d) - %s", peerId, serviceId, error.getMessage());
                    });
        }
    }

    private void stopAdvertising() {
        synchronized (guard) {
            advertising = false;
            client.stopAdvertising();
        }
    }

    private void stopDiscovery() {
        synchronized (guard) {
            discovering = false;
            client.stopDiscovery();
        }
    }

    private void requestConnection(final String endpointId) {
        Timber.v("Request Connection: %s", states.get(endpointId));
        client.requestConnection(
                toString(peerId),
                endpointId,
                this)
                .addOnSuccessListener(ignored -> {
                    Timber.v("Request Connection succeed for (%s)", endpointId);
                    states.put(endpointId, State.REQUEST_SUCCESS);
                    pendingEndpoints.remove(endpointId);
                    requestTries.remove(endpointId);
                })
                .addOnFailureListener(error -> {
                    Timber.v("Request Connection failed for (%s)", endpointId);
                    states.put(endpointId, State.REQUEST_FAILED);
                    pendingEndpoints.insertLastUniquely(endpointId);
                    startRequestThread();
                    Timber.e("Request Connection Error %s", error.getMessage());
                });
        states.put(endpointId, State.REQUESTING);
    }


    // Discovery callback for getting advertised devices
    private final EndpointDiscoveryCallback discoveryCallback = new EndpointDiscoveryCallback() {

        @Override
        public void onEndpointFound(@NonNull String endpointId, @NonNull DiscoveredEndpointInfo info) {
            long serviceId = toLong(info.getServiceId());
            if (Connection.this.serviceId != serviceId) {
                Timber.e("Unknown ServiceId (%d) from EndpointId (%s)", serviceId, info.getEndpointName());
                return;
            }

            long peerId = toLong(info.getEndpointName());
            Timber.e("Found EndpointId (%s) - PeerId (%d)", endpointId, peerId);

            //priority works: remove old endpoints if exists
            if (endpoints.containsKey(peerId)) {
                String oldEndpointId = endpoints.remove(peerId);
                states.remove(oldEndpointId);
                directs.remove(oldEndpointId);
                pendingEndpoints.remove(oldEndpointId);
            }

            endpoints.put(peerId, endpointId);
            states.put(endpointId, State.FOUND);
            pendingEndpoints.insertLastUniquely(endpointId);
            requestTries.put(endpointId, 0);
            startRequestThread();
        }

        @Override
        public void onEndpointLost(@NonNull String endpointId) {
            long peerId = getPeerId(endpointId);
            Timber.v("Endpoint lost (%s) - PeerId (%d)", endpointId, peerId);
            endpoints.remove(peerId);
            states.put(endpointId, State.LOST);
            directs.remove(endpointId);
            pendingEndpoints.remove(endpointId);
            requestTries.remove(endpointId);
        }
    };

    //Payload callback
    private final PayloadCallback payloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(@NonNull String endpointId, @NonNull Payload payload) {
            long peerId = endpoints.inverse().get(endpointId);
            Timber.v("Payload Received from PeerId (%d)", peerId);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    if (callback != null) {
                        callback.onPayload(peerId, payload);
                    }
                }
            });
        }

        @Override
        public void onPayloadTransferUpdate(@NonNull String endpointId, @NonNull PayloadTransferUpdate update) {
            long peerId = endpoints.inverse().get(endpointId);
            Timber.v("Payload Transfer Update from PeerId (%d)", peerId);
            executor.execute(() -> {
                if (callback != null) {
                    callback.onPayloadStatus(peerId, update);
                }
            });
        }
    };


    ////////// Request Threading ////////////////
    private void startRequestThread() {
        synchronized (guard) {
            if (requestThread == null || !requestThread.isRunning()) {
                requestThread = new RequestThread();
                requestThread.start();
            }
            requestThread.notifyRunner();
        }
    }

    private void stopRequestThread() {
        synchronized (guard) {
            if (requestThread != null) {
                requestThread.stop();
            }
        }
    }

    private class RequestThread extends Runner {

        RequestThread() {
            times = Maps.newHashMap();
            delays = Maps.newHashMap();
        }

        @Override
        protected boolean looping() {
            String endpointId = pendingEndpoints.pollFirst();
            long peerId = getPeerId(endpointId);
            Timber.v("Next EndpointId (%s) - PeerId (%d)", endpointId, peerId);
            if (Strings.isNullOrEmpty(endpointId) || peerId == 0L) {
                waitRunner(wait);
                wait += (delayS + delayS);
                return true;
            }
            wait = delayS + delayS;

            //already requested endpoint
            if (states.containsKey(endpointId) && states.get(endpointId) == State.REQUESTING) {
                return true;
            }

            //incoming endpoints: so don't make request on it
            if (directs.containsKey(endpointId) && directs.get(endpointId)) {
                return true;
            }

            if (!times.containsKey(endpointId)) {
                times.put(endpointId, System.currentTimeMillis());
            }

            if (!delays.containsKey(endpointId)) {
                delays.put(endpointId, NearbyUtil.nextRand(5, 15) * delayS);
            }

            if (!requestTries.containsKey(endpointId)) {
                requestTries.put(endpointId, 0);
            }

/*            if (tries.get(endpointId) > maxTry) {
                //TODO
            }*/
            Timber.v("Next Request Attempt...." + endpointId + " " + isExpired(times.get(endpointId), delays.get(endpointId)));
            if (isExpired(times.get(endpointId), delays.get(endpointId))) {
                requestConnection(endpointId);
                times.remove(endpointId);
                delays.remove(endpointId);
                requestTries.put(endpointId, requestTries.get(endpointId) + 1);
            } else {
                pendingEndpoints.insertLastUniquely(endpointId);
            }
            waitRunner(2 * delayS);
            return true;
        }
    }

    //some public api
    boolean send(long peerId, Payload payload) {
        String acceptedEndpoint = getAcceptedEndpointId(peerId);
        if (acceptedEndpoint == null) {
            return false;
        }
        client.sendPayload(acceptedEndpoint, payload);
        return true;
    }

    private long getPeerId(String endpointId) {
        if (endpoints.inverse().containsKey(endpointId)) {
            return endpoints.inverse().get(endpointId);
        }
        return 0;
    }

    private String getAcceptedEndpointId(long peerId) {
        if (!endpoints.containsKey(peerId)) {
            return null;
        }

        String endpointId = endpoints.get(peerId);
        if (!states.containsKey(endpointId)) {
            return null;
        }

        if (states.get(endpointId) != State.ACCEPTED) {
            return null;
        }
        return endpointId;
    }

    private String toString(long value) {
        if (!cache.containsKey(value)) {
            cache.put(value, String.valueOf(value));
        }
        return cache.get(value);
    }

    private long toLong(String value) {
        if (!cache.containsValue(value)) {
            cache.put(Long.parseLong(value), value);
        }
        return cache.inverse().get(value);
    }
}
