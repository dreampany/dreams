package com.dreampany.crypto.api.crypto.model.gecko

import com.dreampany.framework.misc.constant.Constant

/**
 * Created by roman on 10/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class GeckoExchange(
    val id: String = Constant.Default.STRING,
    val name: String = Constant.Default.STRING,
    val country: String? = Constant.Default.NULL,
    val url: String? = Constant.Default.NULL,
    val image: String? = Constant.Default.NULL
)