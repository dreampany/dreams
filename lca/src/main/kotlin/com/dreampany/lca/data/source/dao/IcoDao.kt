package com.dreampany.lca.data.source.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.frame.data.source.dao.BaseDao
import com.dreampany.lca.data.model.Ico
import io.reactivex.Maybe

/**
 * Created by Hawladar Roman on 6/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Dao
interface IcoDao : BaseDao<Ico> {

    @get:Query("select count(*) from ico")
    val count: Int

    @get:Query("select count(*) from ico")
    val countRx: Maybe<Int>

    @get:Query("select * from ico")
    val items: List<Ico>

    @get:Query("select * from ico")
    val itemsRx: Maybe<List<Ico>>

    @Query("select count(*) from ico where id = :id limit 1")
    fun getCount(id: Long): Int

    @Query("select count(*) from ico where id = :id limit 1")
    fun getCountRx(id: Long): Maybe<Int>

    @Query("select * from ico where id = :id limit 1")
    fun getItem(id: Long): Ico

    @Query("select * from ico where id = :id limit 1")
    fun getItemRx(id: Long): Maybe<Ico>

    @Query("select * from ico where status = :status limit :limit")
    fun getItems(status: String, limit: Int): List<Ico>

    @Query("select * from ico where status = :status limit :limit")
    fun getItemsRx(status: String, limit: Int): Maybe<List<Ico>>
}
