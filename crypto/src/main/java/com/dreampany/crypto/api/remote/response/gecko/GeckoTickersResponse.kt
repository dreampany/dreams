package com.dreampany.crypto.api.remote.response.gecko

import com.dreampany.crypto.api.model.gecko.GeckoTicker

/**
 * Created by roman on 11/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class GeckoTickersResponse(
    val name : String,
    val tickers : List<GeckoTicker>
)