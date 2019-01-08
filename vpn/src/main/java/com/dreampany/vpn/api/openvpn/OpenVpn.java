package com.dreampany.vpn.api.openvpn;

/**
 * Created by Hawladar Roman on 10/10/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public interface OpenVpn {

    enum State {
        NO_NETWORK, USER_PAUSE, SCREEN_OFF
    }

    interface StateCallback {
        boolean shouldBeRunning();
    }

    int byteCountInterval = 2;

    void reconnect();

    void pause(State state);

    void resume();

    boolean stopVpn(boolean replaceConnection);

    void networkChange(boolean sameNetwork);

    void setStateCallback(StateCallback callback);
}
