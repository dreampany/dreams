package com.dreampany.tools.api.crypto.model

import com.dreampany.tools.api.crypto.misc.Constants
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 2019-11-12
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class CoinsResponse (
    @SerializedName(value = Constants.Common.STATUS)
    val status: Status,
    @SerializedName(value = Constants.Common.DATA)
    val data: List<Coin>
){
}