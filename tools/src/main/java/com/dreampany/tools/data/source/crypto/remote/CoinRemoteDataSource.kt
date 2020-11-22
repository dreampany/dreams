package com.dreampany.tools.data.source.crypto.remote

import android.content.Context
import com.dreampany.framework.misc.exts.isDebug
import com.dreampany.framework.misc.exts.value
import com.dreampany.framework.misc.func.Keys
import com.dreampany.framework.misc.func.Parser
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.api.crypto.model.cmc.CryptoCoin
import com.dreampany.tools.api.crypto.model.cmc.CryptoPlatform
import com.dreampany.tools.api.crypto.model.cmc.CryptoQuote
import com.dreampany.tools.api.crypto.remote.response.cmc.CoinsResponse
import com.dreampany.tools.api.crypto.remote.response.cmc.QuotesResponse
import com.dreampany.tools.api.crypto.remote.service.CoinMarketCapService
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.model.crypto.Currency
import com.dreampany.tools.data.model.crypto.Quote
import com.dreampany.tools.data.source.crypto.api.CoinDataSource
import com.dreampany.tools.data.source.crypto.mapper.CoinMapper
import com.dreampany.tools.data.source.crypto.mapper.PlatformMapper
import com.dreampany.tools.data.source.crypto.mapper.QuoteMapper
import com.dreampany.tools.misc.constants.Constants
import com.google.common.collect.Maps
import java.net.UnknownHostException

/**
 * Created by roman on 3/21/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CoinRemoteDataSource
constructor(
    private val context: Context,
    private val network: NetworkManager,
    private val parser: Parser,
    private val keys: Keys,
    private val mapper: CoinMapper,
    private val platformMapper: PlatformMapper,
    private val quoteMapper: QuoteMapper,
    private val service: CoinMarketCapService
) : CoinDataSource {

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

/*    @Throws
    override suspend fun reads(
        currency: Currency,
        sort: String,
        order: String,
        @IntRange(from = 0, to = Long.MAX_VALUE)
        offset: Long,
        limit: Long
    ): List<Coin>? {
        for (index in 0..keys.length) {
            try {
                val key = keys.nextKey ?: continue
                val response: Response<CoinsResponse> = service.coins(
                    key.header,
                    currency.name,
                    sort.value,
                    order.value,
                    offset + 1, //Coin Market Cap start from 1 - IntRange
                    limit
                ).execute()
                if (response.isSuccessful) {
                    val data = response.body()?.data ?: return null
                    return mapper.read(data)
                } else {
                    val error = parser.parseError(response, CoinsResponse::class)
                    throw SmartError(
                        message = error?.status?.message,
                        code = error?.status?.code.value
                    )
                }
            } catch (error: Throwable) {
                if (error is SmartError) throw error
                if (error is UnknownHostException) throw SmartError(
                    message = error.message,
                    error = error
                )
                keys.randomForwardKey()
            }
        }
        throw SmartError()
    }

    override suspend fun isFavorite(input: Coin): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFavorite(input: Coin): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun favorites(
        currency: Currency,
        sort: String,
        order: String
    ): List<Coin>? {
        TODO("Not yet implemented")
    }

    override suspend fun write(input: Coin): Long {
        TODO("Not yet implemented")
    }

    override suspend fun write(inputs: List<Coin>): List<Long>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun read(currency: Currency, id: String): Coin? {
        for (index in 0..keys.length) {
            try {
                val key = keys.nextKey ?: continue
                val response = service.quotes(key.header, currency.id, id).execute()
                if (response.isSuccessful) {
                    val data = response.body()?.data ?: return null
                    val inputData = data.get(id) ?: return null
                    return mapper.read(inputData)
                } else {
                    val error = parser.parseError(response, QuotesResponse::class)
                    throw SmartError(
                        message = error?.status?.message,
                        code = error?.status?.code.value
                    )
                }
            } catch (error: Throwable) {
                if (error is SmartError) throw error
                if (error is UnknownHostException) throw SmartError(
                    message = error.message,
                    error = error
                )
                keys.randomForwardKey()
            }
        }
        throw SmartError()
    }

    override suspend fun reads(): List<Coin>? {
        TODO("Not yet implemented")
    }

    override suspend fun reads(currency: Currency, ids: List<String>): List<Coin>? {
        TODO("Not yet implemented")
    }*/


    private val String.header: Map<String, String>
        get() {
            val header = Maps.newHashMap<String, String>()
            header.put(
                Constants.Apis.CoinMarketCap.ACCEPT,
                Constants.Apis.CoinMarketCap.ACCEPT_JSON
            )
            header.put(Constants.Apis.CoinMarketCap.API_KEY, this)
            return header
        }

    override suspend fun isFavorite(input: Coin): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFavorite(input: Coin): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun favorites(currency: Currency, sort: String, order: String): List<Pair<Coin, Quote>>? {
        TODO("Not yet implemented")
    }

    override suspend fun write(input: Pair<Coin, Quote>): Long {
        TODO("Not yet implemented")
    }

    override suspend fun write(inputs: List<Pair<Coin, Quote>>): List<Long>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun read(id: String, currency: Currency): Pair<Coin, Quote>? {
        for (index in 0..keys.length) {
            try {
                val key = keys.nextKey ?: continue
                val response =
                    service.quotes(header = key.header, id = id, convertId = currency.id).execute()
                if (response.isSuccessful) {
                    val data: Map<String, CryptoCoin> = response.body()?.data ?: return null
                    val inputCoin: CryptoCoin = data.get(id) ?: return null
                    val inputPlatform: CryptoPlatform? = inputCoin.platform
                    val inputQuote: CryptoQuote = inputCoin.quote.get(currency.id) ?: return null

                    val coin = mapper.read(inputCoin)
                    val platform = platformMapper.read(inputPlatform)
                    val quote = quoteMapper.read(id, currency, inputQuote)

                    coin.platform = platform
                    return Pair(coin, quote)
                } else {
                    val error = parser.parseError(response, QuotesResponse::class)
                    throw SmartError(
                        message = error?.status?.message,
                        code = error?.status?.code.value
                    )
                }
            } catch (error: Throwable) {
                if (error is SmartError) throw error
                if (error is UnknownHostException) throw SmartError(
                    message = error.message,
                    error = error
                )
                keys.randomForwardKey()
            }
        }
        throw SmartError()
    }

    override suspend fun reads(): List<Pair<Coin, Quote>>? {
        TODO("Not yet implemented")
    }

    override suspend fun reads(ids: List<String>, currency: Currency): List<Pair<Coin, Quote>>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun reads(
        currency: Currency,
        sort: String,
        order: String,
        offset: Long,
        limit: Long
    ): List<Pair<Coin, Quote>>? {
        for (index in 0..keys.length) {
            try {
                val key = keys.nextKey ?: continue
                val response = service.coins(
                    header = key.header,
                    convertId = currency.id,
                    sort = sort.value,
                    order = order.value,
                    offset = offset.inc(), //Coin Market Cap start from 1 - IntRange
                    limit = limit
                ).execute()
                if (response.isSuccessful) {
                    val inputCoins: List<CryptoCoin> = response.body()?.data ?: return null

                    return inputCoins.mapNotNull {
                        val inputPlatform: CryptoPlatform? = it.platform
                        val inputQuote: CryptoQuote? = it.quote.get(currency.id)

                        if (inputQuote == null) {
                            null
                        } else {
                            val coin = mapper.read(it)
                            val platform = platformMapper.read(inputPlatform)
                            val quote = quoteMapper.read(it.id, currency, inputQuote)

                            coin.platform = platform
                            Pair(coin, quote)
                        }
                    }
                } else {
                    val error = parser.parseError(response, CoinsResponse::class)
                    throw SmartError(
                        message = error?.status?.message,
                        code = error?.status?.code.value
                    )
                }
            } catch (error: Throwable) {
                if (error is SmartError) throw error
                if (error is UnknownHostException) throw SmartError(
                    message = error.message,
                    error = error
                )
                keys.randomForwardKey()
            }
        }
        throw SmartError()
    }
}