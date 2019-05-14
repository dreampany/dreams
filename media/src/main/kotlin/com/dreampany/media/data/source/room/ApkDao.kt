package com.dreampany.media.data.source.room

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.frame.data.source.dao.BaseDao
import com.dreampany.media.data.model.Apk
import io.reactivex.Maybe


/**
 * Created by Hawladar Roman on 8/13/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Dao
interface ApkDao : BaseDao<Apk> {

    @get:Query("select count(*) from apk")
    val count: Int

    @get:Query("select count(*) from apk")
    val countRx: Maybe<Int>

    @get:Query("select * from apk order by displayName asc")
    val items: List<Apk>

    @get:Query("select * from apk order by displayName asc")
    val itemsRx: Maybe<List<Apk>>

    @Query("select count(*) from apk where id = :id limit 1")
    fun getCount(id: Long): Int

    @Query("select count(*) from apk where id = :id limit 1")
    fun getCountRx(id: Long): Maybe<Int>

    @Query("select * from apk where id = :id limit 1")
    fun getItem(id: Long): Apk

    @Query("select * from apk where id = :id limit 1")
    fun getItemRx(id: Long): Maybe<Apk>

    @Query("select * from apk order by displayName asc limit :limit")
    fun getItems(limit: Int): List<Apk>

    @Query("select * from apk order by displayName asc limit :limit")
    fun getItemsRx(limit: Int): Maybe<List<Apk>>
}