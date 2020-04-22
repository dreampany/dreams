package com.dreampany.tools.data.source.radio.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.common.data.source.room.dao.BaseDao
import com.dreampany.tools.data.model.radio.Station

/**
 * Created by roman on 21/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface StationDao : BaseDao<Station> {
    @get:Query("select count(*) from station")
    val count: Int

    @get:Query("select * from station")
    val items: List<Station>?

    @Query("select * from station where country_code = :countryCode")
    fun getItems(countryCode: String): List<Station>?
}