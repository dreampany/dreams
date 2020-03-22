package com.dreampany.tools.api.crypto.remote.response

import com.dreampany.tools.api.crypto.misc.CryptoConstants
import com.dreampany.tools.api.crypto.model.CryptoCoin
import com.dreampany.tools.api.crypto.model.CryptoStatus
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 2019-11-16
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class QuotesResponse(
    @SerializedName(value = CryptoConstants.Common.STATUS)
    val cryptoStatus: CryptoStatus,
    @SerializedName(value = CryptoConstants.Common.DATA)
    val data: Map<String, CryptoCoin>
) {
    fun isError(): Boolean {
        return cryptoStatus.errorCode != 0
    }

    fun isEmpty() :Boolean {
        return data.isEmpty()
    }
}