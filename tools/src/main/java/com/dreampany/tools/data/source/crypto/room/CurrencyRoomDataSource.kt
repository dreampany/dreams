package com.dreampany.tools.data.source.crypto.room

import com.dreampany.tools.data.model.crypto.Currency
import com.dreampany.tools.data.source.crypto.api.CurrencyDataSource
import com.dreampany.tools.data.source.crypto.mapper.CurrencyMapper
import com.dreampany.tools.data.source.crypto.room.dao.CurrencyDao

/**
 * Created by roman on 11/19/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CurrencyRoomDataSource(
    private val mapper: CurrencyMapper,
    private val dao: CurrencyDao
) : CurrencyDataSource {

    @Throws
    override suspend fun write(input: Currency): Long {
        mapper.write(input)
        return dao.insertOrReplace(input)
    }

    @Throws
    override suspend fun write(inputs: List<Currency>): List<Long>? = inputs.map { write(it) }

    @Throws
    override suspend fun reads(): List<Currency>? = mapper.reads(dao)
}