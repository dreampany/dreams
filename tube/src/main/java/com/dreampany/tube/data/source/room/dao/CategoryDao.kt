package com.dreampany.tube.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.framework.data.source.room.dao.BaseDao
import com.dreampany.tube.data.model.Category

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface CategoryDao : BaseDao<Category> {
    @get:Query("select count(*) from category")
    val count: Int

    @get:Query("select * from category")
    val items: List<Category>?

    @Query("delete from category")
    fun deleteAll()
}