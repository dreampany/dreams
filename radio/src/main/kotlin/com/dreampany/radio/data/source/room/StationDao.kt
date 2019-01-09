package com.dreampany.radio.data.source.room

/**
 * Created by Hawladar Roman on 4/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.dreampany.frame.data.source.dao.BaseDao
import com.dreampany.radio.data.model.Station
import io.reactivex.Maybe

@Dao
interface StationDao : BaseDao<Station> {

    @get:Query("select count(*) from station")
    val count: Int

    @get:Query("select count(*) from station")
    val countRx: Maybe<Int>

    @get:Query("select * from station order by name asc")
    val items: List<Station>

    @get:Query("select * from station order by name asc")
    val itemsRx: Maybe<List<Station>>
}
