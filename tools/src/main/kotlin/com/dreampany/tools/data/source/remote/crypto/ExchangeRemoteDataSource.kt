package com.dreampany.tools.data.source.remote.crypto

import android.content.Context
import com.dreampany.framework.api.key.KeyManager
import com.dreampany.framework.misc.exception.EmptyException
import com.dreampany.framework.util.AndroidUtil
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.api.crypto.misc.Constants
import com.dreampany.tools.api.crypto.remote.service.CryptoCompareService
import com.dreampany.tools.data.mapper.crypto.ExchangeMapper
import com.dreampany.tools.data.model.crypto.Exchange
import com.dreampany.tools.data.source.api.crypto.ExchangeDataSource
import com.google.common.collect.Maps
import io.reactivex.Maybe
import timber.log.Timber

/**
 * Created by roman on 29/2/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ExchangeRemoteDataSource
constructor(
    private val context: Context,
    private val network: NetworkManager,
    private val keyM: KeyManager,
    private val mapper: ExchangeMapper,
    private val service: CryptoCompareService
) : ExchangeDataSource {

    init {
        if (AndroidUtil.isDebug(context)) {
            keyM.setKeys(
                Constants.CryptoCompare.API_KEY_ROMAN_BJIT
            )
        } else {
            keyM.setKeys(
                Constants.CryptoCompare.API_KEY_ROMAN_BJIT
            )
        }
    }

    override fun getExchanges(
        extraParams: String,
        fromSymbol: String,
        toSymbol: String,
        limit: Long
    ): List<Exchange>? {
        if (network.isObserving() && !network.hasInternet()) {
            return null
        }
        for (index in 0..keyM.length / 2) {
            try {
                val key = keyM.getKey()
                val response = service.getExchanges(getHeader(key), extraParams, fromSymbol, toSymbol, limit).execute()
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        return mapper.getItems(result.data.exchanges)
                    }
                }
            } catch (error: Throwable) {
                Timber.e(error)
                keyM.randomForwardKey()
            }
        }
        return null
    }

    override fun getExchangesRx(
        extraParams: String,
        fromSymbol: String,
        toSymbol: String,
        limit: Long
    ): Maybe<List<Exchange>> {
        return Maybe.create { emitter ->
            val result = getExchanges(extraParams, fromSymbol, toSymbol, limit)
            if (emitter.isDisposed) return@create
            if (result == null) {
                emitter.onError(EmptyException())
            } else {
                emitter.onSuccess(result)
            }
        }
    }


    /* private */
    fun getHeader(key: String): Map<String, String> {
        val header = Maps.newHashMap<String, String>()
        header.put(Constants.CryptoCompare.AUTHORIZATION, key)
        return header
    }
}