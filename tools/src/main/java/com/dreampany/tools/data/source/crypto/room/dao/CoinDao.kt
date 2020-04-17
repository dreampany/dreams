package com.dreampany.tools.data.source.crypto.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.common.data.source.room.dao.BaseDao
import com.dreampany.tools.data.model.crypto.Coin

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface CoinDao : BaseDao<Coin> {
    @get:Query("select count(*) from coin")
    val count: Int

    @get:Query("select * from coin")
    val items: List<Coin>?
}