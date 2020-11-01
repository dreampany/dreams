package com.dreampany.radio.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.framework.data.source.room.dao.BaseDao
import com.dreampany.radio.data.model.Station

/**
 * Created by roman on 2/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface StationDao : BaseDao<Station> {
    @get:Query("select * from station order by last_check_ok_time desc")
    val all: List<Station>?
}