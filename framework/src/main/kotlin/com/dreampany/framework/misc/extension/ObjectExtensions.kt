package com.dreampany.framework.misc.extension

import com.google.common.hash.Hashing
import java.util.*

/**
 * Created by roman on 2020-01-28
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

fun String?.hash() : String {
    return UUID.randomUUID().toString()
}

fun String?.hash512(): String {
    if (this.isNullOrEmpty()) return this.hash()
    return Hashing.sha512().newHasher()
        .putString(this, Charsets.UTF_8).hash().toString()
}
