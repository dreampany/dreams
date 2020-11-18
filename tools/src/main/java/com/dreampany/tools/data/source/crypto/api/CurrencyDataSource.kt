package com.dreampany.tools.data.source.crypto.api

import com.dreampany.tools.data.model.crypto.Currency

/**
 * Created by roman on 11/18/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface CurrencyDataSource {
    @Throws
    suspend fun write(input: Currency): Long

    @Throws
    suspend fun write(inputs: List<Currency>): List<Long>?
}