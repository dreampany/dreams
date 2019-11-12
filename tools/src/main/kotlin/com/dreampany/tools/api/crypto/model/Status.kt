package com.dreampany.tools.api.crypto.model

import com.dreampany.tools.api.crypto.misc.Constants
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 2019-11-12
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class Status(
    @SerializedName(value = Constants.Status.ERROR_CODE)
    val errorCode: Int,
    @SerializedName(value = Constants.Status.ERROR_MESSAGE)
    val errorMessage: String,
    val elapsed: Int,
    @SerializedName(value = Constants.Status.CREDIT_COUNT)
    val creditCount: Int,
    val timestamp: String
) {
}