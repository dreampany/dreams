package com.dreampany.tools.api.crypto.model.cmc

import com.dreampany.tools.api.crypto.misc.Constants
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 11/16/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class CryptoPlatform(
    val id: String,
    val name: String,
    val symbol: String,
    val slug: String,
    @SerializedName(Constants.Keys.CMC.TOKEN_ADDRESS)
    val tokenAddress: String
)