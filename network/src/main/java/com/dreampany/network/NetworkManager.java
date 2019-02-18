package com.dreampany.network;

import android.content.Context;

import com.dreampany.network.data.model.Network;
import com.dreampany.network.misc.RxMapper;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import hugo.weaving.DebugLog;
import timber.log.Timber;

/**
 * Created by Hawladar Roman on 8/18/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Singleton
public final class NetworkManager {

    public interface Callback {
        void onResult(Network... networks);
    }

    private final Context context;
    private final RxMapper rx;
    private final WifiApi wifi;
    private final WifiApApi ap;
    private final BluetoothApi bt;
    private final InternetApi it;
    private volatile boolean internet;
    private final Set<Callback> callbacks;
    private final Map<Callback, Boolean> checkInternets;

    private boolean resultFired;

    @Inject
    NetworkManager(Context context,
                   RxMapper rx,
                   WifiApi wifi,
                   WifiApApi ap,
                   BluetoothApi bt,
                   InternetApi it) {
        this.context = context.getApplicationContext();
        this.rx = rx;
        this.wifi = wifi;
        this.ap = ap;
        this.bt = bt;
        this.it = it;
        callbacks = Sets.newConcurrentHashSet();
        checkInternets = Maps.newConcurrentMap();

        resultFired = false;
    }

    @DebugLog
    void onResult(boolean internet) {
        resultFired = true;
        this.internet = internet;
        postActiveNetworks();
    }

    public void observe(Callback callback, boolean checkInternet) {
        callbacks.add(callback);
        checkInternets.put(callback, checkInternet);
        startInternetIfPossible();

        Timber.v("Internet Callbacks %d", callbacks.size());
    }

    public void deObserve(Callback callback, boolean stopInternetCheck) {
        callbacks.remove(callback);
        if (stopInternetCheck) {
            checkInternets.remove(callback);
        }
        stopInternetIfPossible();
    }

    public boolean isObserving() {
        return resultFired && !callbacks.isEmpty();
    }

    public List<Network> getNetworks() {
        List<Network> networks = new ArrayList<>();
        networks.add(wifi.getNetwork(internet));
        networks.add(ap.getNetwork(internet));
        networks.add(bt.getNetwork(internet));
        return networks;
    }

    public List<Network> getActiveNetworks() {
        List<Network> networks = new ArrayList<>();
        if (wifi.isEnabled()) {
            networks.add(wifi.getNetwork(internet));
        }
        if (ap.isEnabled()) {
            networks.add(ap.getNetwork(internet));
        }
/*        if (bt.isEnabled()) {
            networks.add(bt.getNetwork(internet));
        }*/
        return networks;
    }

    public boolean hasInternet() {
        return internet;
    }

    private void startInternetIfPossible() {
        for (Map.Entry<Callback, Boolean> entry : checkInternets.entrySet()) {
            if (entry.getValue()) {
                it.start(this::onResult);
                break;
            }
        }
    }

    private void stopInternetIfPossible() {
        boolean checkInternet = false;
        for (Map.Entry<Callback, Boolean> entry : checkInternets.entrySet()) {
            if (entry.getValue()) {
                checkInternet = true;
                break;
            }
        }
        if (!checkInternet) {
            it.stop(this::onResult);
        }
    }

    private void postActiveNetworks() {
        List<Network> result = getActiveNetworks();
        Network[] networks = result.toArray(new Network[0]);
        for (Callback callback : callbacks) {
            callback.onResult(networks);
        }
    }
}
