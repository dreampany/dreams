/*
package com.dreampany.network.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.dreampany.network.data.enums.NetworkType;
import com.dreampany.network.misc.Constants;

*/
/**
 * Created by Hawladar Roman on 8/18/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 *//*

@Entity(indices = {@Index(value = {"bssid", "ssid"}, unique = true)},
        primaryKeys = {"bssid", "ssid"})
public class Network implements Parcelable {

    private NetworkType type;
    private String bssid;
    private String ssid;
    private String capabilities;
    private boolean enabled;
    private boolean connected;
    private boolean internet;

    @Ignore
    public Network() {

    }

    public Network(NetworkType type) {
        this.type = type;
    }

    @Ignore
    private Network(Parcel in) {

    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

    }

    public static final Creator<Network> CREATOR = new Creator<Network>() {
        @Override
        public Network createFromParcel(Parcel in) {
            return new Network(in);
        }

        @Override
        public Network[] newArray(int size) {
            return new Network[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("network#");
        builder.append(" type: ").append(type);
        builder.append(" bssid: ").append(bssid);
        builder.append(" ssid: ").append(ssid);
        builder.append(" open: ").append(isOpen());
        builder.append(" enabled: ").append(enabled);
        builder.append(" connected: ").append(connected);
        builder.append(" internet: ").append(internet);
        return builder.toString();
    }

    public void setType(NetworkType type) {
        this.type = type;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void setInternet(boolean internet) {
        this.internet = internet;
    }

    public NetworkType getType() {
        return type;
    }

    public String getBssid() {
        return bssid;
    }

    public String getSsid() {
        return ssid;
    }

    public String getCapabilities() {
        return capabilities;
    }

    public boolean isOpen() {
        return capabilities == null ||
                !(capabilities.contains(Constants.SECURITY.WEP) ||
                        capabilities.contains(Constants.SECURITY.PSK) ||
                        capabilities.contains(Constants.SECURITY.EAP));
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean hasInternet() {
        return internet;
    }
}
*/
