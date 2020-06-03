package com.dreampany.crypto.api.remote.response

import com.dreampany.crypto.api.misc.ApiConstants
import com.dreampany.crypto.api.model.ExchangesData
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 29/2/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class ExchangesResponse(
    @SerializedName(ApiConstants.Exchange.DATA)
    val data: ExchangesData
) {
}