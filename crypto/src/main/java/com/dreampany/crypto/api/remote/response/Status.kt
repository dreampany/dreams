package com.dreampany.crypto.api.remote.response

import com.dreampany.crypto.api.misc.ApiConstants
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 17/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class Status(
    var timestamp: String? = null,
    @SerializedName(value = ApiConstants.Status.ERROR_CODE)
    var errorCode: Int? = null,
    @SerializedName(value = ApiConstants.Status.ERROR_MESSAGE)
    var errorMessage: String? = null,
    var elapsed: Long? = null,
    @SerializedName(value = ApiConstants.Status.CREDIT_COUNT)
    var creditCount: Long? = null
)