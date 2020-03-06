package com.dreampany.common.misc.extension

import com.google.common.hash.Hashing

/**
 * Created by roman on 3/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
fun String.hash256(): String {
    return Hashing.sha256().newHasher()
        .putString(this, Charsets.UTF_8).hash().toString()
}