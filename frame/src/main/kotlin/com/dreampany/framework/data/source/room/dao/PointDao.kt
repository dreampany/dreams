package com.dreampany.framework.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.model.Point
import io.reactivex.Maybe

/**
 * Created by Hawladar Roman on 3/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */

@Dao
interface PointDao : BaseDao<Point> {

    @get:Query("select count(*) from point")
    val count: Int

    @get:Query("select count(*) from point")
    val countRx: Maybe<Int>

    @get:Query("select * from point")
    val items: List<Point>?

    @get:Query("select * from point")
    val itemsRx: Maybe<List<Point>>

    @Query("select sum(credit) from point limit 1")
    fun getCredits() : Int

    @Query("select sum(credit) from point where type = :type limit 1")
    fun getCredits(type: Type) : Int

    @Query("select sum(credit) from point where type = :type and subtype = :subtype limit 1")
    fun getCredits(type: Type, subtype: Subtype) : Int

    @Query("select * from point where id = :id and type = :type and subtype = :subtype and level = :level limit 1")
    fun getItem(id: String, type: String, subtype: String, level: String): Point?

    @Query("select * from point where id = :id and type = :type and subtype = :subtype and level = :level limit 1")
    fun getItemRx(id: String, type: String, subtype: String, level: String): Maybe<Point>
}