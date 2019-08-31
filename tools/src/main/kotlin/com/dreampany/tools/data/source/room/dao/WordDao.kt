package com.dreampany.tools.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.frame.data.source.room.dao.BaseDao
import com.dreampany.tools.data.model.Word
import io.reactivex.Maybe

/**
 * Created by roman on 2019-08-16
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface WordDao: BaseDao<Word> {

    @Query("select * from word where id = :id limit 1")
    fun getItem(id: String): Word?

    @Query("select * from word where id = :id limit 1")
    fun getItemRx(id: String): Maybe<Word>

    @Query("select * from word")
    fun getItemsRx(): Maybe<List<Word>>

    @Query("select id from word order by id asc")
    fun getRawItemsRx(): Maybe<List<String>>
}