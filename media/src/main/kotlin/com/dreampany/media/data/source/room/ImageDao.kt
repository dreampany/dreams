package com.dreampany.media.data.source.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.dreampany.frame.data.source.dao.BaseDao
import com.dreampany.media.data.model.Image
import io.reactivex.Maybe


/**
 * Created by Hawladar Roman on 8/13/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Dao
interface ImageDao : BaseDao<Image> {

    @get:Query("select count(*) from image")
    val count: Int

    @get:Query("select count(*) from image")
    val countRx: Maybe<Int>

    @get:Query("select * from image order by displayName asc")
    val items: List<Image>

    @get:Query("select * from image order by displayName asc")
    val itemsRx: Maybe<List<Image>>

    @Query("select count(*) from image where id = :id limit 1")
    fun getCount(id: Long): Int

    @Query("select count(*) from image where id = :id limit 1")
    fun getCountRx(id: Long): Maybe<Int>

    @Query("select * from image where id = :id limit 1")
    fun getItem(id: Long): Image

    @Query("select * from image where id = :id limit 1")
    fun getItemRx(id: Long): Maybe<Image>

    @Query("select * from image order by displayName asc limit :limit")
    fun getItems(limit: Int): List<Image>

    @Query("select * from image order by displayName asc limit :limit")
    fun getItemsRx(limit: Int): Maybe<List<Image>>
}