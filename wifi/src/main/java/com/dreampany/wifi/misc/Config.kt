package com.dreampany.wifi.misc

/**
 * Created by roman on 22/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Config() {
    private val SIZE_MIN = 1024
    private val SIZE_MAX = 4096

    var size: Int = SIZE_MAX

    val isSizeAvailable : Boolean get() = size == SIZE_MAX
}