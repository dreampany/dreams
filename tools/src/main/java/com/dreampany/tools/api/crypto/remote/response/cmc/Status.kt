package com.dreampany.tools.api.crypto.remote.response.cmc

import com.dreampany.framework.misc.constant.Constant
import com.dreampany.tools.api.crypto.misc.Constants
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 17/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class Status(
    val timestamp: String = Constant.Default.STRING,
    @SerializedName(value = Constants.Keys.Status.ERROR_CODE)
    val errorCode: Int = Constant.Default.INT,
    @SerializedName(value = Constants.Keys.Status.ERROR_MESSAGE)
    val errorMessage: String? = Constant.Default.NULL,
    val elapsed: Int = Constant.Default.INT,
    @SerializedName(value = Constants.Keys.Status.CREDIT_COUNT)
    val creditCount: Int = Constant.Default.INT,
    val notice : String? = Constant.Default.NULL
)