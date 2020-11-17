package com.dreampany.tools.data.source.crypto.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.framework.data.source.room.dao.BaseDao
import com.dreampany.tools.data.model.crypto.Quote

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface QuoteDao : BaseDao<Quote> {
    @Query("select * from quote where id = :id and currency = :currency limit 1")
    fun read(id: String, currency: String): Quote?
}