package com.dreampany.tools.api.crypto.remote.response

import com.dreampany.tools.api.crypto.misc.Constants
import com.dreampany.tools.api.crypto.model.CryptoTrade
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 29/2/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class TradesResponse(
    @SerializedName(Constants.Trade.DATA)
    val data: List<CryptoTrade>
)