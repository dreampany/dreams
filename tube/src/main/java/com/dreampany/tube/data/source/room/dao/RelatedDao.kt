package com.dreampany.tube.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.framework.data.source.room.dao.BaseDao
import com.dreampany.tube.data.model.Related

/**
 * Created by roman on 21/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface RelatedDao : BaseDao<Related> {
    @Query("select * from related where lefter = :part or righter = :part")
    fun getItems(part: String): List<Related>
}