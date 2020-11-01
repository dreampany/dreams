package com.dreampany.radio.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.framework.data.source.room.dao.BaseDao
import com.dreampany.radio.data.model.Page

/**
 * Created by roman on 1/11/20
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