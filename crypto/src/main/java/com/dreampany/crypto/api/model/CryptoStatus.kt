package com.dreampany.crypto.api.model

import com.dreampany.crypto.api.misc.ApiConstants
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 2019-11-12
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class CryptoStatus(
    @SerializedName(value = ApiConstants.Status.ERROR_CODE)
    val errorCode: Int,
    @SerializedName(value = ApiConstants.Status.ERROR_MESSAGE)
    val errorMessage: String,
    val elapsed: Int,
    @SerializedName(value = ApiConstants.Status.CREDIT_COUNT)
    val creditCount: Int,
    val timestamp: String
)