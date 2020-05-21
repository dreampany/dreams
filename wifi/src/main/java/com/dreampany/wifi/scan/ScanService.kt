package com.dreampany.wifi.scan

import com.dreampany.wifi.data.model.Wifi

/**
 * Created by roman on 20/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface ScanService {
    interface ScanCallback {
        fun onResult(wifi: Wifi)
    }

    val isScanning: Boolean
    fun start()
    fun startScan()
    fun stopScan()
    fun stop()
    fun takeScanResults()
}