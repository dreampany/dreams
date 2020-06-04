package com.dreampany.crypto.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.framework.data.source.room.dao.BaseDao
import com.dreampany.crypto.data.model.Quote

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface QuoteDao : BaseDao<Quote> {
    @Query("select * from quote where id = :id and currency = :currency limit 1")
    fun getItem(id: String, currency: String): Quote?
}