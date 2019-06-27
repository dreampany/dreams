package com.dreampany.network.api;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.network.data.model.Network;

import javax.inject.Inject;

/**
 * Created by Hawladar Roman on 8/18/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class WifiApi implements NetworkApi {

    private final Context context;
    private final WifiManager wifi;

    @Inject
    WifiApi(Context context,
            RxMapper rx,
            AppExecutors ex) {
        this.context = context;
        wifi = (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public Network getNetwork(boolean internet) {
        Network network = new Network(Network.Type.WIFI);
        network.setEnabled(isEnabled());
        network.setConnected(isConnected());
        network.setInternet(internet);
        return network;
    }

    public boolean isEnabled() {
        return wifi.isWifiEnabled();
    }

    public boolean isConnected() {
        if (isEnabled()) {
            WifiInfo wifiInfo = wifi.getConnectionInfo();
            return wifiInfo.getNetworkId() != -1;
        } else {
            return false;
        }
    }
}
