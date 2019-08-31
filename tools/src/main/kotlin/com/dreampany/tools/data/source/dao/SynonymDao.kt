package com.dreampany.tools.data.source.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.frame.data.source.dao.BaseDao
import com.dreampany.tools.data.model.Synonym
import io.reactivex.Maybe

/**
 * Created by roman on 2019-08-31
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface SynonymDao : BaseDao<Synonym> {
    @get:Query("select count(*) from synonym")
    val count: Int

    @get:Query("select count(*) from synonym")
    val countRx: Maybe<Int>

    @get:Query("select * from synonym")
    val items: List<Synonym>?

    @get:Query("select * from synonym")
    val itemsRx: Maybe<List<Synonym>>

    @Query("select * from synonym where lefter = :word or righter = :word")
    fun getItems(word: String): List<Synonym>?

    @Query("select * from synonym where lefter = :word or righter = :word")
    fun getItemsRx(word: String): Maybe<List<Synonym>>
}