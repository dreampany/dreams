package com.dreampany.frame.data.source.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.frame.data.model.ImageLink

/**
 * Created by Roman-372 on 7/30/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface ImageLinkDao : BaseDao<ImageLink> {

    @Query("select * from imagelink where ref = :ref and url = :url limit 1")
    fun getItem(ref: String, url: String): ImageLink?

    @Query("select * from imagelink where ref = :ref")
    fun getItems(ref: String): List<ImageLink>?
}