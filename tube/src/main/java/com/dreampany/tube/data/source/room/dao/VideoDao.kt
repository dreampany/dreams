package com.dreampany.tube.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.framework.data.source.room.dao.BaseDao
import com.dreampany.tube.data.model.Video

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface VideoDao : BaseDao<Video> {
    @get:Query("select count(*) from video")
    val count: Int

    @get:Query("select * from video")
    val items: List<Video>?

    @Query("select count(*) from video where id = :id limit 1")
    fun count(id: String): Int

    @Query("select * from video where id = :id")
    fun read(id: String): Video?

    @Query("select * from video where id in (:ids)")
    fun reads(ids: List<String>): List<Video>?

    @Query("select * from video where categoryId = :categoryId")
    fun getsOfCategoryId(categoryId: String): List<Video>?
}