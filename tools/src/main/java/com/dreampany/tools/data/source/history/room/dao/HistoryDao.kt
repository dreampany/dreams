package com.dreampany.tools.data.source.history.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.framework.data.source.room.dao.BaseDao
import com.dreampany.tools.data.enums.history.HistorySource
import com.dreampany.tools.data.enums.history.HistoryState
import com.dreampany.tools.data.model.history.History

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface HistoryDao : BaseDao<History> {
    @get:Query("select count(*) from history")
    val count: Int

    @get:Query("select * from history")
    val items: List<History>?

    @Query("select * from history where source = :source and state = :state and month = :month and day = :day")
    fun getItems(source: HistorySource, state: HistoryState, month: Int, day: Int): List<History>?
}