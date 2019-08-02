package com.dreampany.history.data.source.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.frame.data.source.dao.BaseDao
import com.dreampany.history.data.enums.LinkSource
import com.dreampany.history.data.model.ImageLink
import io.reactivex.Maybe

/**
 * Created by Roman-372 on 7/30/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface ImageLinkDao : BaseDao<ImageLink> {

    @Query("select * from imagelink where id = :id limit 1")
    fun getItem(id: String): ImageLink?

    @Query("select * from imagelink where source = :source and ref = :ref")
    fun getItems(source: LinkSource, ref: String): List<ImageLink>?

    @Query("select * from imagelink where source = :source and ref = :ref")
    fun getItemsRx(source: LinkSource, ref: String): Maybe<List<ImageLink>>
}