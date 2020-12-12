package com.dreampany.framework.misc.func

import java.net.UnknownHostException

/**
 * Created by roman on 17/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
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