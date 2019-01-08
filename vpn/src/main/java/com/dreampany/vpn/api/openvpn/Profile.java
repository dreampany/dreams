package com.dreampany.vpn.api.openvpn;

import java.io.Serializable;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 * Created by Hawladar Roman on 10/10/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class Profile implements Serializable, Cloneable {

    public enum Type {
        CERTIFICATES, PKCS12, KEYSTORE, USERPASS, STATICKEYS, USERPASS_CERTIFICATES, USERPASS_PKCS12, USERPASS_KEYSTORE
    }

    public enum X509 {
        VERIFY_TLSREMOTE, ERIFY_TLSREMOTE_COMPAT_NOREMAPPING, VERIFY_TLSREMOTE_DN, VERIFY_TLSREMOTE_RDN, VERIFY_TLSREMOTE_RDN_PREFIX
    }

    public static transient final long MAX_EMBED_FILE_SIZE = 2048 * 1024; // 2048kB

    public static final String EXTRA_PROFILEUUID = "de.blinkt.openvpn.profileUUID";
    public static final String INLINE_TAG = "[[INLINE]]";
    public static final String DISPLAYNAME_TAG = "[[NAME]]";

    public static final int MAXLOGLEVEL = 4;
    public static final int CURRENT_PROFILE_VERSION = 6;
    public static final int DEFAULT_MSSFIX_SIZE = 1280;
    public static String DEFAULT_DNS1 = "8.8.8.8";
    public static String DEFAULT_DNS2 = "8.8.4.4";


    public transient String transientPW = null;
    public transient String transientPCKS12PW = null;

    public transient boolean profileDeleted = false;
    public Type authType = Type.KEYSTORE;
    public String name;
    public String alias;
    public String clientCertFilename;
    public String tlsAuthDirection = "";
    public String tlsAuthFilename;
    public String clientKeyFilename;
    public String caFilename;
    public boolean useLzo = true;
    public String pkcs12Filename;
    public String pkcs12Password;
    public boolean useTLSAuth = false;

    public String dns1 = DEFAULT_DNS1;
    public String dns2 = DEFAULT_DNS2;
    public String ipv4Address;
    public String ipv6Address;
    public boolean overrideDns = false;
    public String searchDomain = "blinkt.de";

    public boolean useDefaultRoute = true;
    public boolean usePull = true;
    public String customRoutes;
    public boolean checkRemoteCN = true;
    public boolean expectTLSCert = false;
    public String remoteCN = "";
    public String password = "";
    public String username = "";
    public boolean routeNoPull = false;
    public boolean useRandomHostname = false;
    public boolean useFloat = false;
    public boolean useCustomConfig = false;
    public String customConfigOptions = "";
    public String verb = "1";  //ignored
    public String cipher = "";
    public boolean noBind = false;
    public boolean useDefaultRouteV6 = true;
    public String customRoutesV6 = "";
    public String keyPassword = "";
    public boolean persistTun = false;
    public String connectRetryMax = "-1";
    public String connectRetry = "2";
    public String connectRetryMaxTime = "300";
    public boolean userEditable = true;
    public String auth = "";
    public X509 x509AuthType = X509.VERIFY_TLSREMOTE_RDN;
    public String mx509UsernameField = null;

    private transient PrivateKey privateKey;
    private UUID uuid;
    public boolean allowLocalLAN;
    private int profileVersion;
    public String excludedRoutes;
    public String excludedRoutesV6;
    public int mssFix = 0; // -1 is default,
    public final List<Connection> connections;
    public boolean remoteRandom = false;
    public HashSet<String> allowedAppsVpn = new HashSet<>();
    public boolean allowedAppsVpnAreDisallowed = true;

    public Profile(String name) {
        this.name = name;
        uuid = UUID.randomUUID();

        profileVersion = CURRENT_PROFILE_VERSION;
        connections = new ArrayList<>();
        connections.add(new Connection());
    }

    @Override
    public String toString() {
        return name;
    }

    public String getUuidString() {
        return uuid.toString();
    }
}
