package com.dreampany.crypto.api.remote.response.cmc

import com.dreampany.framework.data.enums.Code

/**
 * Created by roman on 17/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class Response<T> {
    var status: Status? = null
    var data: T? = null

    val hasError: Boolean
        get() = status?.code != Code.SUCCESS.code

    val isEmpty: Boolean
        get() {
            val raw = data ?: return true
            if (raw is List<*>) raw.isEmpty()
            if (raw is Array<*>) raw.isEmpty()
            return false
        }
}