package com.dreampany.pair.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.common.data.source.room.dao.BaseDao
import com.dreampany.pair.data.model.User

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface UserDao : BaseDao<User> {
    @Query("select count(*) from user where id = :id limit 1")
    fun getCount(id: String): Int

    @Query("select * from user where id = :id limit 1")
    fun getUser(id: String): User?

    @Query("select * from user where email = :email limit 1")
    fun getUserByEmail(email: String): User?

    @Query("select * from user")
    fun getUsers(): List<User>?
}