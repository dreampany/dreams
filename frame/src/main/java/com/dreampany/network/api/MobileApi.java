package com.dreampany.network.api;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.dreampany.network.data.model.Network;

import javax.inject.Inject;

/**
 * Created by roman on 3/1/19
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
public class MobileApi implements NetworkApi {

    private final Context context;
    private final TelephonyManager manager;

    @Inject
    MobileApi(Context context) {
        this.context = context.getApplicationContext();
        manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
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

    public boolean isEnabled() {
        if (manager.getSimState() == TelephonyManager.SIM_STATE_READY) {
            return Settings.Secure.getInt(context.getContentResolver(), "mobile_data", 1) == 1;
        }
        return false;
    }
}
