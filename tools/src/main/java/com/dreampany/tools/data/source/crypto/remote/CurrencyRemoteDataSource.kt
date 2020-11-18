package com.dreampany.tools.data.source.crypto.remote

import android.content.Context
import com.dreampany.framework.misc.exts.isDebug
import com.dreampany.framework.misc.func.Keys
import com.dreampany.framework.misc.func.Parser
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.api.crypto.remote.service.CoinMarketCapService
import com.dreampany.tools.data.model.crypto.Currency
import com.dreampany.tools.data.source.crypto.api.CoinDataSource
import com.dreampany.tools.data.source.crypto.api.CurrencyDataSource
import com.dreampany.tools.data.source.crypto.mapper.CoinMapper
import com.dreampany.tools.misc.constants.Constants

/**
 * Created by roman on 11/18/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CurrencyRemoteDataSource
constructor(
    private val context: Context,
    private val network: NetworkManager,
    private val parser: Parser,
    private val keys: Keys,
    private val mapper: CoinMapper,
    private val service: CoinMarketCapService
) : CurrencyDataSource {

    init {
        if (context.isDebug) {
            keys.setKeys(
                Constants.Apis.CoinMarketCap.CMC_PRO_ROMAN_BJIT
            )
        } else {
            keys.setKeys(
                Constants.Apis.CoinMarketCap.CMC_PRO_DREAM_DEBUG_2,
                Constants.Apis.CoinMarketCap.CMC_PRO_DREAM_DEBUG_1,
                Constants.Apis.CoinMarketCap.CMC_PRO_ROMAN_BJIT,
                Constants.Apis.CoinMarketCap.CMC_PRO_IFTE_NET,
                Constants.Apis.CoinMarketCap.CMC_PRO_DREAMPANY
            )
        }
    }

    override suspend fun write(input: Currency): Long {
        TODO("Not yet implemented")
    }

    override suspend fun write(inputs: List<Currency>): List<Long>? {
        TODO("Not yet implemented")
    }


}