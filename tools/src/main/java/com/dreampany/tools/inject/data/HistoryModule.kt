package com.dreampany.tools.inject.data

import android.app.Application
import android.content.Context
import com.dreampany.framework.inject.annote.Remote
import com.dreampany.framework.inject.annote.Room
import com.dreampany.framework.misc.func.Keys
import com.dreampany.framework.misc.func.Parser
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.api.crypto.inject.data.CoinMarketCapModule
import com.dreampany.tools.api.crypto.inject.data.CryptoCompareModule
import com.dreampany.tools.api.crypto.remote.service.CoinMarketCapService
import com.dreampany.tools.api.crypto.remote.service.CryptoCompareService
import com.dreampany.tools.api.history.inject.MuffinModule
import com.dreampany.tools.data.source.crypto.api.CoinDataSource
import com.dreampany.tools.data.source.crypto.api.ExchangeDataSource
import com.dreampany.tools.data.source.crypto.api.TradeDataSource
import com.dreampany.tools.data.source.crypto.mapper.CoinMapper
import com.dreampany.tools.data.source.crypto.mapper.ExchangeMapper
import com.dreampany.tools.data.source.crypto.mapper.TradeMapper
import com.dreampany.tools.data.source.crypto.remote.CoinRemoteDataSource
import com.dreampany.tools.data.source.crypto.remote.ExchangeRemoteDataSource
import com.dreampany.tools.data.source.crypto.remote.TradeRemoteDataSource
import com.dreampany.tools.data.source.crypto.room.CoinRoomDataSource
import com.dreampany.tools.data.source.crypto.room.dao.CoinDao
import com.dreampany.tools.data.source.crypto.room.dao.QuoteDao
import com.dreampany.tools.data.source.crypto.room.database.DatabaseManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module(
    includes = [
        MuffinModule::class
    ]
)
class HistoryModule {


}