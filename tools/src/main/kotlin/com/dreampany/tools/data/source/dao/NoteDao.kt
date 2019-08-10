package com.dreampany.tools.data.source.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.frame.data.source.dao.BaseDao
import com.dreampany.tools.data.model.Note
import io.reactivex.Maybe

/**
 * Created by Roman-372 on 8/5/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface NoteDao : BaseDao<Note> {

    @Query("select * from note where id = :id limit 1")
    fun getItemRx(id: String): Maybe<Note>

    @Query("select * from note")
    fun getItemsRx(): Maybe<List<Note>>
}