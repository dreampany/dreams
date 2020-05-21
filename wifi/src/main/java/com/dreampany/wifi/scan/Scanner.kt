package com.dreampany.wifi.scan

import android.os.Handler
import com.dreampany.wifi.data.source.WifiRepo

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

    private val periodicScan: PeriodicScan

    init {
        periodicScan = PeriodicScan(this, handler)
    }

    override val isScanning: Boolean get() = periodicScan.scanning

    override fun start() {
        repo.enable
    }

    override fun startScan() {
        TODO("Not yet implemented")
    }

    override fun stopScan() {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }

    override fun takeScanResults() {
         try {
             if (repo.startScan) {
                 val result = repo.scanResults
                 val wifiInfo = repo.wifiInfo
             }
         } catch (error : Throwable) {}
    }


}