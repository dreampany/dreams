package com.dreampany.tools.data.source.repository.crypto

import com.dreampany.framework.data.misc.StoreMapper
import com.dreampany.framework.data.source.repository.Repository
import com.dreampany.framework.data.source.repository.StoreRepository
import com.dreampany.framework.misc.ResponseMapper
import com.dreampany.framework.misc.RxMapper
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.mapper.crypto.TradeMapper
import com.dreampany.tools.data.model.crypto.Exchange
import com.dreampany.tools.data.source.api.crypto.ExchangeDataSource
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
class ExchangeRepository
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    private val network: NetworkManager,
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepository,
    private val mapper: TradeMapper
): Repository<String, Exchange>(rx, rm), ExchangeDataSource {
    override fun getExchanges(
        extraParams: String,
        fromSymbol: String,
        toSymbol: String,
        limit: Long
    ): List<Exchange>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getExchangesRx(
        extraParams: String,
        fromSymbol: String,
        toSymbol: String,
        limit: Long
    ): Maybe<List<Exchange>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}