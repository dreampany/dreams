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

    override val isScanning: Boolean
        get() = TODO("Not yet implemented")

    override fun start() {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }
}