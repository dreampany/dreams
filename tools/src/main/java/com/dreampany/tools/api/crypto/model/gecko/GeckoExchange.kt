package com.dreampany.tools.api.crypto.model.gecko

import com.dreampany.framework.misc.constant.Constants

/**
 * Created by roman on 10/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class GeckoExchange(
    val id: String = Constants.Default.STRING,
    val name: String = Constants.Default.STRING,
    val country: String? = Constants.Default.NULL,
    val url: String? = Constants.Default.NULL,
    val image: String? = Constants.Default.NULL
)