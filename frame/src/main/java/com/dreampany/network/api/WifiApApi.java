package com.dreampany.network.api;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import com.dreampany.network.data.enums.ApState;
import com.dreampany.network.data.model.Network;

import java.lang.reflect.Method;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Hawladar Roman on 8/18/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class WifiApApi implements NetworkApi {

    private final Context context;
    private final WifiManager wifi;

    @Inject
    WifiApApi(Context context) {
        this.context = context.getApplicationContext();
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
        network.setInternet(internet);
        return network;
    }

    public ApState getState() {
        try {
            Method method = wifi.getClass().getMethod("getWifiApState");

            int tmp = ((Integer) method.invoke(wifi));

            // Fix for Android 4
            if (tmp >= 10) {
                tmp = tmp - 10;
            }

            return ApState.class.getEnumConstants()[tmp];
        } catch (Exception e) {
            Timber.e(e);
            return ApState.FAILED;
        }
    }

    public boolean isEnabled() {
        return getState() == ApState.ENABLED;
    }

    public WifiConfiguration getConfiguration() {
        try {
            Method method = wifi.getClass().getMethod("getWifiApConfiguration");
            return (WifiConfiguration) method.invoke(wifi);
        } catch (Exception e) {
            Timber.e(e);
            return null;
        }
    }

    public boolean setConfiguration(WifiConfiguration wifiConfig) {
        try {
            Method method = wifi.getClass().getMethod("setWifiApConfiguration", WifiConfiguration.class);
            return (Boolean) method.invoke(wifi, wifiConfig);
        } catch (Exception e) {
            Timber.e(e);
            return false;
        }
    }


}
