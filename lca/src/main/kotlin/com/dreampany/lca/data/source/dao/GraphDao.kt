package com.dreampany.lca.data.source.dao


import androidx.room.Dao
import androidx.room.Query
import com.dreampany.frame.data.source.dao.BaseDao
import com.dreampany.lca.data.model.Graph
import io.reactivex.Maybe

/**
 * Created by Hawladar Roman on 4/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Dao
interface GraphDao : BaseDao<Graph> {

    @get:Query("select count(*) from graph")
    val count: Int

    @get:Query("select count(*) from graph")
    val countRx: Maybe<Int>

    @get:Query("select * from graph")
    val items: List<Graph>

    @get:Query("select * from graph")
    val itemsRx: Maybe<List<Graph>>

    @Query("select count(*) from graph where id = :id limit 1")
    fun getCount(id: Long): Int

    @Query("select count(*) from graph where id = :id limit 1")
    fun getCountRx(id: Long): Maybe<Int>

    @Query("select * from graph where id = :id limit 1")
    fun getItem(id: Long): Graph

    @Query("select * from graph where id = :id limit 1")
    fun getItemRx(id: Long): Maybe<Graph>

    @Query("select * from graph where slug = :slug and startTime = :startTime and endTime = :endTime limit 1")
    fun getItem(slug: String, startTime: Long, endTime: Long): Graph

    @Query("select * from graph where slug = :slug and startTime = :startTime and endTime = :endTime limit 1")
    fun getItemRx(slug: String, startTime: Long, endTime: Long): Maybe<Graph>
}
