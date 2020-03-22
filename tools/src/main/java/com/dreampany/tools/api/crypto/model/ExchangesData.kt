package com.dreampany.tools.api.crypto.model

import com.dreampany.tools.api.crypto.misc.CryptoConstants
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 29/2/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class ExchangesData(
    @SerializedName(CryptoConstants.Exchange.EXCHANGES)
    val exchanges: List<CryptoExchange>
) {
}