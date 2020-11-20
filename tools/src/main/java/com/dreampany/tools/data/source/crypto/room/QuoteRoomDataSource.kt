package com.dreampany.tools.data.source.crypto.room

import com.dreampany.tools.data.model.crypto.Currency
import com.dreampany.tools.data.model.crypto.Quote
import com.dreampany.tools.data.source.crypto.api.QuoteDataSource
import com.dreampany.tools.data.source.crypto.mapper.QuoteMapper
import com.dreampany.tools.data.source.crypto.room.dao.QuoteDao

/**
 * Created by roman on 11/21/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class QuoteRoomDataSource(
    private val mapper: QuoteMapper,
    private val dao: QuoteDao
) : QuoteDataSource {

    @Throws
    override suspend fun write(input: Quote): Long {
        mapper.write(input)
        return dao.insertOrReplace(input)
    }

    @Throws
    override suspend fun write(inputs: List<Quote>): List<Long>? = inputs.map { write(it) }

    @Throws
    override suspend fun read(id: String, currency: Currency): Quote? =
        mapper.read(id, currency, dao)

    @Throws
    override suspend fun reads(ids: List<String>, currencies: List<Currency>): List<Quote>? {
        TODO("Not yet implemented")
    }

}