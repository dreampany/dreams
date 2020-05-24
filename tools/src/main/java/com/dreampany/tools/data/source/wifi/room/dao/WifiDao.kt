package com.dreampany.tools.data.source.wifi.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.framework.data.source.room.dao.BaseDao
import com.dreampany.tools.data.model.wifi.Wifi

/**
 * Created by roman on 24/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface WifiDao : BaseDao<Wifi> {
    @get:Query("select count(*) from wifi")
    val count: Int

    @get:Query("select * from wifi")
    val items: List<Wifi>?
}