package com.dreampany.tools.data.source.repository.crypto

import com.dreampany.framework.data.misc.StoreMapper
import com.dreampany.framework.data.source.repository.Repository
import com.dreampany.framework.data.source.repository.StoreRepository
import com.dreampany.framework.injector.annote.Database
import com.dreampany.framework.injector.annote.Remote
import com.dreampany.framework.injector.annote.Room
import com.dreampany.framework.misc.ResponseMapper
import com.dreampany.framework.misc.RxMapper
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.mapper.crypto.TradeMapper
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.model.crypto.Trade
import com.dreampany.tools.data.source.api.crypto.TradeDataSource
import io.reactivex.Maybe
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 29/2/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class TradeRepository
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    private val network: NetworkManager,
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepository,
    private val mapper: TradeMapper,
    @Room private val room: TradeDataSource,
    @Database private val database: TradeDataSource,
    @Remote private val remote: TradeDataSource
) : Repository<String, Trade>(rx, rm), TradeDataSource {
    override fun getTrades(extraParams: String, fromSymbol: String, limit: Long): List<Trade>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTradesRx(
        extraParams: String,
        fromSymbol: String,
        limit: Long
    ): Maybe<List<Trade>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}