package com.dreampany.tools.api.crypto.remote.response

import com.dreampany.tools.misc.constants.CryptoConstants
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 17/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class Status(
    var timestamp: String? = null,
    @SerializedName(value = CryptoConstants.Status.ERROR_CODE)
    var errorCode: Int? = null,
    @SerializedName(value = CryptoConstants.Status.ERROR_MESSAGE)
    var errorMessage: String? = null,
    var elapsed: Long? = null,
    @SerializedName(value = CryptoConstants.Status.CREDIT_COUNT)
    var creditCount: Long? = null
)