package com.dreampany.hello.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.framework.data.source.room.dao.BaseDao
import com.dreampany.hello.data.model.User

/**
 * Created by roman on 26/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface UserDao: BaseDao<User> {
    @get:Query("select count(*) from user")
    val count: Int

    @get:Query("select * from user")
    val items: List<User>?
}