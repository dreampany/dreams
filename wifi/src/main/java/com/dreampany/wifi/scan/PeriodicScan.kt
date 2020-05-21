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

) : Runnable {
    private val DELAY_INITIAL = 1L
    private val DELAY_INTERVAL = 1000L

    private val
}