package com.dreampany.frame.data.source.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import io.reactivex.Maybe

/**
 * Created by Roman-372 on 7/4/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface BaseDaoKt<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(t: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplaceRx(t: T): Maybe<Long>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertOrAbort(t: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrIgnore(t: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(ts: List<T>): List<Long>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertOrAbort(ts: List<T>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrIgnore(ts: List<T>): List<Long>

    @Update
    fun update(t: T): Int

    @Delete
    fun delete(t: T): Int
}