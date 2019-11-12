package com.dreampany.tools.data.source.remote

import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.source.api.CoinDataSource
import javax.inject.Singleton

/**
 * Created by roman on 2019-11-12
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class RemoteCoinDataSource
constructor(
    val network: NetworkManager,
    val mapper: CoinMapper,
    val service: CmcService
) : CoinDataSource {
}