package com.dreampany.crypto.api.model.gecko

import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 10/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class GeckoMarket(
    @SerializedName("identifier")
    val id: String,
    val name: String
)