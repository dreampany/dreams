package com.dreampany.tools.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.framework.data.source.room.dao.BaseDao
import com.dreampany.tools.data.model.Contact
import io.reactivex.Maybe

/**
 * Created by roman on 2019-11-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface ContactDao : BaseDao<Contact> {

    @get:Query("select count(*) from contact")
    val count: Int

    @get:Query("select * from contact")
    val items: List<Contact>?

    @get:Query("select * from contact")
    val itemsRx: Maybe<List<Contact>>

    @Query("select * from contact where id = :id limit 1")
    fun getItem(id: String): Contact?

    @Query("select * from contact where id = :id limit 1")
    fun getItemRx(id: String): Maybe<Contact>
}