package com.dreampany.framework.data.enums

/**
 * Created by roman on 17/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
enum class Code(val code: Int) {
    SUCCESS(200),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    MANY_REQUESTS(429),
    SERVER_ERROR(500);

    val isSuccess: Boolean
        get() = this == SUCCESS

    val isBadRequest: Boolean
        get() = this == BAD_REQUEST

    val isUnauthorized: Boolean
        get() = this == UNAUTHORIZED

    val isForbidden: Boolean
        get() = this == FORBIDDEN

    val isManyRequests: Boolean
        get() = this == MANY_REQUESTS

    val isServerError: Boolean
        get() = this == SERVER_ERROR
}