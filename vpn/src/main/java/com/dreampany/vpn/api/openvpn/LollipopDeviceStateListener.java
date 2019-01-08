package com.dreampany.vpn.api.openvpn;

import android.annotation.TargetApi;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;


/**
 * Created by Hawladar Roman on 10/5/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class LollipopDeviceStateListener extends ConnectivityManager.NetworkCallback {

    private String network;
    private String properties;
    private String capabilities;

    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);
        if (!network.toString().equals(this.network)) {
            this.network = network.toString();
        }
    }

    @Override
    public void onLinkPropertiesChanged(Network network, LinkProperties properties) {
        super.onLinkPropertiesChanged(network, properties);
        if (!properties.toString().equals(this.properties)) {
            this.properties = properties.toString();
        }
    }

    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities capabilities) {
        super.onCapabilitiesChanged(network, capabilities);
        if (!capabilities.toString().equals(this.capabilities)) {
            this.capabilities = capabilities.toString();
        }
    }
}
