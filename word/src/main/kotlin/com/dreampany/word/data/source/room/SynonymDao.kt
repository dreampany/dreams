package com.dreampany.word.data.source.room

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.frame.data.source.dao.BaseDao
import com.dreampany.word.data.model.Synonym
import io.reactivex.Flowable
import io.reactivex.Single


/**
 * Created by Hawladar Roman on 9/5/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Dao
interface SynonymDao : BaseDao<Synonym> {
    @get:Query("select count(*) from synonym")
    val count: Int

    @get:Query("select count(*) from synonym")
    val countRx: Single<Int>

    @get:Query("select * from synonym")
    val items: List<Synonym>

    @get:Query("select * from synonym")
    val itemsRx: Flowable<List<Synonym>>

    @Query("select * from synonym where lefter = :word or righter = :word")
    fun getItems(word: String): List<Synonym>

    @Query("select * from synonym where lefter = :word or righter = :word")
    fun getItemsRx(word: String): Single<List<Synonym>>
}