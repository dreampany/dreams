package com.dreampany.tools.data.source.note.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.framework.data.source.room.dao.BaseDao
import com.dreampany.tools.data.model.note.Note

/**
 * Created by roman on 4/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface NoteDao : BaseDao<Note> {
    @get:Query("select count(*) from note")
    val count: Int

    @get:Query("select * from note")
    val items: List<Note>?
}