package com.dreampany.tools.data.source.crypto.repo

import com.dreampany.framework.inject.annote.Remote
import com.dreampany.framework.inject.annote.Room
import com.dreampany.tools.data.model.crypto.Currency
import com.dreampany.tools.data.source.crypto.api.CurrencyDataSource
import com.dreampany.tools.data.source.crypto.mapper.CurrencyMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 11/19/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class CurrencyRepo
@Inject constructor(
    private val mapper: CurrencyMapper,
    @Room private val room: CurrencyDataSource,
    @Remote private val remote: CurrencyDataSource
) : CurrencyDataSource {

    override suspend fun write(input: Currency): Long {
        TODO("Not yet implemented")
    }

    override suspend fun write(inputs: List<Currency>): List<Long>? {
        TODO("Not yet implemented")
    }

    @Throws
    @Synchronized
    override suspend fun reads(): List<Currency>? = withContext(Dispatchers.IO) {
        var result: List<Currency>? = null
        if (mapper.isExpired()) {
            result = remote.reads()
            if (!result.isNullOrEmpty()) {
                mapper.writeExpire()
                room.write(result)
            }
        }
        if (result.isNullOrEmpty())
            result = room.reads()
        result
    }

}