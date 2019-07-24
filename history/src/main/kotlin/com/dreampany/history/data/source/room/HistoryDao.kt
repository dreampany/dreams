package com.dreampany.history.data.source.room

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.frame.data.source.dao.BaseDao
import com.dreampany.history.data.enums.HistoryType
import com.dreampany.history.data.model.History

/**
 * Created by roman on 2019-07-24
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface HistoryDao: BaseDao<History> {

    @Query("select * from history where id = :id limit 1")
    fun getItem(id: String): History?

    @Query("select * from history where type = :type and day = :day and month = :month")
    fun getItems(type: HistoryType, day: Int, month: Int): List<History>?
}