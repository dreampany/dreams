package com.dreampany.vpn.api.openvpn;

import com.dreampany.frame.util.DataUtil;

import java.io.Serializable;
import java.util.Locale;

/**
 * Created by Hawladar Roman on 10/10/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class Connection implements Serializable, Cloneable {
    public static final int CONNECTION_DEFAULT_TIMEOUT = 120;

    public String serverName = "openvpn.blinkt.de";
    public int serverPort = 1194;
    public boolean useUdp = true;
    public String customConfig = "";
    public boolean useCustomConfig = false;
    public boolean enabled = true;
    public int connectTimeout = 0;

    public Connection() {

    }

    @Override
    public Connection clone() throws CloneNotSupportedException {
        return (Connection) super.clone();
    }


    @Override
    public String toString() {
        StringBuilder block = new StringBuilder();

        block.append("Server ").append(serverName).append(" ").append(serverPort);
        if (useUdp) {
            block.append(" udp\n");
        } else {
            block.append(" tcp-client\n");
        }

        if (connectTimeout != 0) {
            block.append(String.format(Locale.ENGLISH, " connect-timeout  %d\n", connectTimeout));
        }

        if (useCustomConfig && !DataUtil.isEmpty(customConfig)) {
            block.append(customConfig);
            block.append("\n");
        }

        return block.toString();
    }

    public boolean isOnlyServer() {
        return !useCustomConfig || DataUtil.isEmpty(customConfig);
    }

    public int getTimeout() {
        if (connectTimeout <= 0)
            return CONNECTION_DEFAULT_TIMEOUT;
        else
            return connectTimeout;
    }
}
