package com.dreampany.tools.api.crypto.model

import com.dreampany.tools.api.crypto.misc.CryptoConstants
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 2019-11-12
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class CryptoStatus(
    @SerializedName(value = CryptoConstants.Status.ERROR_CODE)
    val errorCode: Int,
    @SerializedName(value = CryptoConstants.Status.ERROR_MESSAGE)
    val errorMessage: String,
    val elapsed: Int,
    @SerializedName(value = CryptoConstants.Status.CREDIT_COUNT)
    val creditCount: Int,
    val timestamp: String
)