package com.dreampany.wifi.scan

import android.os.Handler
import kotlinx.coroutines.Runnable

/**
 * Created by roman on 21/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class PeriodicScan(
    private val service: ScanService,
    private val handler: Handler,
    var scanning: Boolean = false
) : Runnable {

    private val DELAY_INITIAL = 1L
    private val DELAY_INTERVAL = 1000L

    override fun run() {
        service.start()
        service.takeScanResults()
        nextRun(5 * DELAY_INTERVAL)
    }

    fun start() {
        nextRun(DELAY_INITIAL)
    }

    fun stop() {
        handler.removeCallbacks(this)
        scanning = false
    }

    private fun nextRun(delayInitial: Long) {
        stop()
        handler.postDelayed(this, delayInitial)
        scanning = true
    }
}