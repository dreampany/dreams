package com.dreampany.crypto.api.crypto.remote.response.cc

import com.dreampany.crypto.api.crypto.misc.Constants
import com.dreampany.crypto.api.crypto.model.cc.ExchangesData
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 29/2/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class ExchangesResponse(
    @SerializedName(Constants.Keys.Exchange.DATA)
    val data: ExchangesData
)