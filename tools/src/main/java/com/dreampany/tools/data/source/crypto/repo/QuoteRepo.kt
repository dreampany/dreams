package com.dreampany.tools.data.source.crypto.repo

import com.dreampany.framework.inject.annote.Remote
import com.dreampany.framework.inject.annote.Room
import com.dreampany.tools.data.model.crypto.Currency
import com.dreampany.tools.data.model.crypto.Quote
import com.dreampany.tools.data.source.crypto.api.QuoteDataSource
import com.dreampany.tools.data.source.crypto.mapper.QuoteMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 11/21/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class QuoteRepo
@Inject constructor(
    private val mapper: QuoteMapper,
    @Room private val room: QuoteDataSource,
    @Remote private val remote: QuoteDataSource
) : QuoteDataSource {
    override suspend fun write(input: Quote): Long {
        TODO("Not yet implemented")
    }

    override suspend fun write(inputs: List<Quote>): List<Long>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun read(id: String, currency: Currency): Quote? =
        withContext(Dispatchers.IO) {
            var result: Quote? = null
            if (mapper.isExpired(id, currency)) {
                result = remote.read(id, currency)
                result?.let {
                    mapper.writeExpire(id, currency)
                    room.write(it)
                }
            }
            if (result == null)
                result = room.read(id, currency)
            result
        }

    override suspend fun reads(ids: List<String>, currencies: List<Currency>): List<Quote>? {
        TODO("Not yet implemented")
    }
}