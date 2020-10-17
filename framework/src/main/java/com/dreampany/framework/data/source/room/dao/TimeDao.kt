package com.dreampany.framework.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.framework.data.model.Time

/**
 * Created by roman on 1/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface TimeDao : BaseDao<Time> {

    @Query("select * from time where id = :id and type = :type and subtype = :subtype and state = :state limit 1")
    fun read(id: String, type: String, subtype: String, state: String): Time?

    @Query("select time from time where id = :id and type = :type and subtype = :subtype and state = :state limit 1")
    fun readTime(id: String, type: String, subtype: String, state: String): Long
}