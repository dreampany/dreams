package com.dreampany.framework.data.source.room.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(input: T): Long

/*    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplaceRx(t: T): Maybe<Long>*/

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertOrAbort(input: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrIgnore(input: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(inputs: List<T>): List<Long>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertOrAbort(inputs: List<T>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrIgnore(inputs: List<T>): List<Long>

    @Update
    fun update(input: T): Int

    @Delete
    fun delete(input: T): Int
}