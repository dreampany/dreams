package com.dreampany.tools.api.crypto.remote.response

import com.dreampany.tools.misc.constant.CryptoConstants
import com.dreampany.tools.api.crypto.model.CryptoTrade
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 29/2/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class TradesResponse(
    @SerializedName(CryptoConstants.Trade.DATA)
    val data: List<CryptoTrade>
)