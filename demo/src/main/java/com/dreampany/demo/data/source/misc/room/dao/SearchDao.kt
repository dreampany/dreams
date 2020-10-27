package com.dreampany.demo.data.source.misc.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.framework.data.source.room.dao.BaseDao
import com.dreampany.demo.data.model.misc.Search

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface SearchDao : BaseDao<Search> {

    @get:Query("select * from search")
    val all: List<Search>?

    @Query("select * from search where id = :id limit 1")
    fun read(id: String): Search?
}