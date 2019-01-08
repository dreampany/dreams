package com.dreampany.lca.data.source.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.dreampany.frame.data.source.dao.BaseDao
import com.dreampany.lca.data.model.News
import io.reactivex.Maybe

/**
 * Created by Hawladar Roman on 6/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Dao
interface NewsDao : BaseDao<News> {

    @get:Query("select count(*) from news")
    val count: Int

    @get:Query("select count(*) from news")
    val countRx: Maybe<Int>

    @get:Query("select * from news")
    val items: List<News>

    @get:Query("select * from news")
    val itemsRx: Maybe<List<News>>

    @Query("select count(*) from news where id = :id limit 1")
    fun getCount(id: Long): Int

    @Query("select count(*) from news where id = :id limit 1")
    fun getCountRx(id: Long): Maybe<Int>

    @Query("select * from news where id = :id limit 1")
    fun getItem(id: Long): News

    @Query("select * from news where id = :id limit 1")
    fun getItemRx(id: Long): Maybe<News>

    @Query("select * from news order by publishedOn limit :limit")
    fun getItems(limit: Int): List<News>

    @Query("select * from news order by publishedOn limit :limit")
    fun getItemsRx(limit: Int): Maybe<List<News>>
}
