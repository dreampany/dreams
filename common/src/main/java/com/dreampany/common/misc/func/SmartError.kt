package com.dreampany.common.misc.func

import com.dreampany.common.data.enums.ErrorCode
import java.net.UnknownHostException

/**
 * Created by roman on 17/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class SmartError(
    override val message: String? = null,
    val code: ErrorCode? = null,
    val error: Throwable? = null
) : Throwable(message = message) {

    val hostError : Boolean
        get() = error is UnknownHostException
}