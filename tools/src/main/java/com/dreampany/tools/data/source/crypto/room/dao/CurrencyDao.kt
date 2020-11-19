package com.dreampany.tools.data.source.crypto.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.framework.data.source.room.dao.BaseDao
import com.dreampany.tools.data.model.crypto.Currency

/**
 * Created by roman on 11/19/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface CurrencyDao : BaseDao<Currency> {
    @get:Query("select * from currency")
    val all: List<Currency>?
}