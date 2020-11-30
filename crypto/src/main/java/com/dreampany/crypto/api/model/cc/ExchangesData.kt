package com.dreampany.crypto.api.model.cc

import com.dreampany.crypto.api.misc.Constants
import com.dreampany.crypto.api.model.cc.CryptoExchange
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