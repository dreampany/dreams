package com.dreampany.crypto.api.model.cmc

import com.dreampany.crypto.api.misc.Constants
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 11/16/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class CryptoMeta(
    val id: String,
    val name: String,
    val symbol: String,
    val slug: String,
    val logo: String,
    val description: String,
    val notice: String?,
    val category: String,
    val tags: List<String>?,
    val platform: CryptoPlatform?,
    val urls: Map<String, List<String>>?= null,
    @SerializedName(Constants.Keys.CMC.DATE_ADDED)
    val dateAdded: String
)