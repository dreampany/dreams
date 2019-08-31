package com.dreampany.word.data.source.room

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.frame.data.source.room.dao.BaseDao
import com.dreampany.word.data.model.Antonym
import io.reactivex.Maybe


/**
 * Created by Hawladar Roman on 9/5/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Dao
interface AntonymDao : BaseDao<Antonym> {
    @get:Query("select count(*) from antonym")
    val count: Int

    @get:Query("select count(*) from antonym")
    val countRx: Maybe<Int>

    @get:Query("select * from antonym")
    val items: List<Antonym>

    @get:Query("select * from antonym")
    val itemsRx: Maybe<List<Antonym>>

    @Query("select * from antonym where lefter = :word or righter = :word")
    fun getItems(word: String): List<Antonym>

    @Query("select * from antonym where lefter = :word or righter = :word")
    fun getItemsRx(word: String): Maybe<List<Antonym>>
}