package com.dreampany.vpn.api.openvpn;

import android.annotation.SuppressLint;

import com.dreampany.frame.util.DataUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedList;

import timber.log.Timber;

/**
 * Created by Hawladar Roman on 10/10/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class OpenVpnThread implements Runnable {

    private static final String LIB_PATH = "LIB_PATH";
    private static final String DUMP_PATH_STRING = "Dump path: ";
    @SuppressLint("SdCardPath")
    private static final String BROKEN_PIE_SUPPORT = "/data/data/com.dreampany.vpn/cache/vpn";
    private final static String BROKEN_PIE_SUPPORT2 = "syntax error";

    public static final int M_FATAL = (1 << 4);
    public static final int M_NONFATAL = (1 << 5);
    public static final int M_WARN = (1 << 6);
    public static final int M_DEBUG = (1 << 7);

    private String[] argv;
    private Process process;
    private String nativeDir;
    private OpenVpnService service;
    private String dumpPath;
    private boolean brokenPie = false;
    private boolean noProcessExitStatus = false;

    public OpenVpnThread(OpenVpnService service, String[] argv, String nativeDir) {
        this.service = service;
        this.argv = argv;
        this.nativeDir = nativeDir;
    }

    @Override
    public void run() {
        try {
            Timber.d("Starting OpenVpn");
            startOpenVpnThreadArgs(argv);
            Timber.d("OpenVpn process exited");
        } catch (Exception error) {
            Timber.e(error, "Error in OpenVpnThread %s", error.toString());
        } finally {

        }
    }

    public void stopProcess() {
        process.destroy();
    }

    void setReplaceConnection() {
        noProcessExitStatus = true;
    }

    private void startOpenVpnThreadArgs(String[] argv) {
        LinkedList<String> args = new LinkedList<>();
        Collections.addAll(args, argv);

        ProcessBuilder builder = new ProcessBuilder(args);
        String path = getLibraryPath(argv, builder);
        builder.environment().put(LIB_PATH, path);
        builder.redirectErrorStream(true);

        try {
            process = builder.start();
            process.getOutputStream().close();

            InputStream is = process.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            while (true) {
                String log = br.readLine();
                if (log == null)
                    return;

                if (log.startsWith(DUMP_PATH_STRING))
                    dumpPath = log.substring(DUMP_PATH_STRING.length());

                if (log.startsWith(BROKEN_PIE_SUPPORT) || log.contains(BROKEN_PIE_SUPPORT2))
                    brokenPie = true;

            }

        } catch (IOException error) {
            //VpnStatus.logException("Error reading from output of OpenVpn process", e);
            Timber.e(error, "Error reading from output of OpenVpn process %s", error.toString());
            stopProcess();
        }
    }

    private String getLibraryPath(String[] argv, ProcessBuilder builder) {
        String libPath = argv[0].replaceFirst("/cache/.*$", "/lib");
        String path = builder.environment().get(LIB_PATH);

        if (DataUtil.isEmpty(path))
            path = libPath;
        else
            path = libPath + ":" + path;

        if (!libPath.equals(nativeDir)) {
            path = nativeDir + ":" + path;
        }
        return path;
    }
}
