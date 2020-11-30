package com.dreampany.crypto.api.crypto.model.cc

import com.dreampany.crypto.api.crypto.misc.Constants
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 29/2/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class ExchangesData(
    @SerializedName(Constants.Keys.Exchange.EXCHANGES)
    val exchanges: List<CryptoExchange>
) {
}