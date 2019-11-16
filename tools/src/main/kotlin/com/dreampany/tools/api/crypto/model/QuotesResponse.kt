package com.dreampany.tools.api.crypto.model

import com.dreampany.tools.api.crypto.misc.Constants
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 2019-11-16
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class QuotesResponse(
    @SerializedName(value = Constants.Common.STATUS)
    val status: Status,
    @SerializedName(value = Constants.Common.DATA)
    val data: Map<String, Coin>
) {
    fun isError(): Boolean {
        return status.errorCode != 0
    }

    fun isEmpty() :Boolean {
        return data.isEmpty()
    }
}