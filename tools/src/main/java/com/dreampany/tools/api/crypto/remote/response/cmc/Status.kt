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
    var timestamp: String? = Constant.Default.NULL,
    @SerializedName(value = Constants.Keys.Status.ERROR_CODE)
    var errorCode: Int = Constant.Default.INT,
    @SerializedName(value = Constants.Keys.Status.ERROR_MESSAGE)
    var errorMessage: String = Constant.Default.STRING,
    var elapsed: Int = Constant.Default.INT,
    @SerializedName(value = Constants.Keys.Status.CREDIT_COUNT)
    var creditCount: Int = Constant.Default.INT
)