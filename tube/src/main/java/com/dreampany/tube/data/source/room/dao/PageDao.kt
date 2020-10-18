package com.dreampany.tube.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.framework.data.source.room.dao.BaseDao
import com.dreampany.tube.data.model.Page

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface PageDao : BaseDao<Page> {

    @get:Query("select * from page")
    val all: List<Page>?

    @Query("select * from page where id = :id limit 1")
    fun read(id: String): Page?
}