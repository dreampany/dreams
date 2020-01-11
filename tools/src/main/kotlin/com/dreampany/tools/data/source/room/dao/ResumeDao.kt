package com.dreampany.tools.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.framework.data.source.room.dao.BaseDao
import com.dreampany.tools.data.model.Resume
import com.dreampany.tools.data.model.Server
import io.reactivex.Maybe

/**
 * Created by roman on 2020-01-12
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface ResumeDao : BaseDao<Resume> {

    @Query("select * from resume where id = :id limit 1")
    fun getItem(id: String): Resume?

    @Query("select * from resume where id = :id limit 1")
    fun getItemRx(id: String): Maybe<Resume>

    @Query("select * from resume")
    fun getItems(): List<Resume>?

    @Query("select * from resume")
    fun getItemsRx(): Maybe<List<Resume>>
}