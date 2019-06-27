package com.dreampany.network.api;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.dreampany.network.data.model.Network;

import javax.inject.Inject;

/**
 * Created by Hawladar Roman on 8/18/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class BluetoothApi implements NetworkApi {

    private static final int REQUEST_ENABLE_BT = 1111;

    private final Context context;
    private BluetoothManager manager;
    private BluetoothAdapter adapter;

    @Inject
    BluetoothApi(Context context) {
        this.context = context.getApplicationContext();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            if (manager != null) {
                adapter = manager.getAdapter();
            }
        } else {
            adapter = BluetoothAdapter.getDefaultAdapter();
        }
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

    public void showEnableDialog(Activity activity) {
        if (!isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    public boolean isEnabled() {
        if (adapter != null) {
            return adapter.isEnabled();
        }
        return false;
    }

    public void enable() {
        if (!isEnabled()) {
            adapter.enable();
        }
    }

    public void disable() {
        if (isEnabled()) {
            adapter.disable();
        }
    }
}
