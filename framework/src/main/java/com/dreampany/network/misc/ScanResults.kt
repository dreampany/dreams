package com.dreampany.network.misc

import android.net.wifi.ScanResult
import android.os.Build

/**
 * Created by roman on 16/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Throws
fun centerFreq0(input: ScanResult): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        input.centerFreq0
    } else throw IllegalArgumentException("")
}

@Throws
fun channelWidth(input: ScanResult): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        input.channelWidth
    } else throw IllegalArgumentException("")
}