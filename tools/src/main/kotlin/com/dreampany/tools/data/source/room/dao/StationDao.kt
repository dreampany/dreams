package com.dreampany.tools.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.framework.data.source.room.dao.BaseDao
import com.dreampany.tools.data.model.Station
import io.reactivex.Maybe

/**
 * Created by roman on 2019-10-11
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface StationDao : BaseDao<Station> {

    @Query(value = "select * from station where country_code = :countryCode")
    fun getItemsByCountryCode(countryCode: String): List<Station>?

    @Query(value = "select * from station where country_code = :countryCode")
    fun getItemsByCountryCodeRx(countryCode: String): Maybe<List<Station>>
}