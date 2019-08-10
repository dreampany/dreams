package com.dreampany.tools.service

import android.net.VpnService
import android.os.ParcelFileDescriptor
import com.dreampany.tools.R
import com.dreampany.tools.misc.Constants
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import java.util.concurrent.*
import java.io.FileDescriptor
import com.dreampany.tools.data.model.Packet


/**
 * Created by roman on 2019-08-10
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class VpnService : VpnService() {

    private var running = false
    private var vpnInterface: ParcelFileDescriptor? = null
    private var pendingIntent: PendingIntent? = null

    private val executorService: ExecutorService? = null

    override fun onCreate() {
        super.onCreate()
        running = true
        setupVpn()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }

    override fun onDestroy() {

        super.onDestroy()
    }

    private fun setupVpn() {
        if (vpnInterface == null) {
            val builder = Builder()
            builder.addAddress(Constants.Service.VPN_ADDRESS, 32)
            builder.addRoute(Constants.Service.VPN_ROUTE, 0)
            vpnInterface = builder.setSession(getString(com.dreampany.tools.R.string.app_name)).setConfigureIntent(pendingIntent).establish();
        }
    }

    private class VpnRunner : Runnable {

        private lateinit var descriptor: FileDescriptor
        private lateinit var toNetworkUdpQueue: ConcurrentLinkedQueue<Packet>

        override fun run() {


        }
    }
}