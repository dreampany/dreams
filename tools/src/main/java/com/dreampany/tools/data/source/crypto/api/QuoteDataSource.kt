package com.dreampany.tools.data.source.crypto.api

import com.dreampany.tools.data.model.crypto.Currency
import com.dreampany.tools.data.model.crypto.Quote

/**
 * Created by roman on 11/20/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface QuoteDataSource {

    @Throws
    suspend fun write(input: Quote): Long

    @Throws
    suspend fun write(inputs: List<Quote>): List<Long>?

    @Throws
    suspend fun read(id: String, currency: Currency): Quote?

    @Throws
    suspend fun reads(ids: List<String>, currencies: List<Currency>): List<Quote>?
}