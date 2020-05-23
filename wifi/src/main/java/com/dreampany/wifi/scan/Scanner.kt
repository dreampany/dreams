package com.dreampany.wifi.scan

import android.os.Handler
import com.dreampany.wifi.data.source.WifiRepo
import com.dreampany.wifi.misc.Config

/**
 * Created by roman on 21/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Scanner(
    private val repo: WifiRepo,
    private val handler: Handler
) : ScanService {


    private val cache: Cache
    private val periodicScan: PeriodicScan

    init {
        cache = Cache(Config())
        periodicScan = PeriodicScan(this, handler)
    }

    override val isScanning: Boolean get() = periodicScan.scanning

    override fun start() {
        repo.enable
    }

    override fun startScan() {
        periodicScan.start()
    }

    override fun stopScan() {
        periodicScan.stop()
    }

    override fun stop() {
        repo.disable
    }

    override fun takeScanResults() {
         try {
             if (repo.startScan) {
                 cache.add(repo.scanResults, repo.wifiInfo)
             }
         } catch (error : Throwable) {}


    }


}