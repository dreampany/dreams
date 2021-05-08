package com.dreampany.hi.misc

import java.net.UnknownHostException

/**
 * Created by roman on 5/8/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
data class SmartError(
    override val message: String? = null,
    val code: Int = 0,
    val error: Throwable? = null
) : Throwable(message = message) {

    val hostError: Boolean
        get() = error is UnknownHostException

    val isForbidden: Boolean
        get() = code == 403
}