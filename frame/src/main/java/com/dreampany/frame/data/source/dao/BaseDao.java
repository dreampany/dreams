package com.dreampany.frame.data.source.dao;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Hawladar Roman on 29/4/18.
 * Dreampany
 * dreampanymail@gmail.com
 */
public interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertOrReplace(T t);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insertOrAbort(T t);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertOrIgnore(T t);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertOrReplace(List<T> ts);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    List<Long> insertOrAbort(List<T> ts);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    List<Long> insertOrIgnore(List<T> ts);

    @Update
    int update(T item);

    @Delete
    int delete(T item);
}
