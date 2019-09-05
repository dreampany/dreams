package com.dreampany.tools.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.framework.data.source.room.dao.BaseDao
import com.dreampany.tools.data.model.Antonym
import io.reactivex.Maybe

/**
 * Created by roman on 2019-08-31
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface AntonymDao : BaseDao<Antonym> {
    @get:Query("select count(*) from antonym")
    val count: Int

    @get:Query("select count(*) from antonym")
    val countRx: Maybe<Int>

    @get:Query("select * from antonym")
    val items: List<Antonym>?

    @get:Query("select * from antonym")
    val itemsRx: Maybe<List<Antonym>>

    @Query("select * from antonym where lefter = :word or righter = :word")
    fun getItems(word: String): List<Antonym>?

    @Query("select * from antonym where lefter = :word or righter = :word")
    fun getItemsRx(word: String): Maybe<List<Antonym>>
}