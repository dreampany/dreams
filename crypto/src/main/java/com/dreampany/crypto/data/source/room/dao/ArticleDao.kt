package com.dreampany.crypto.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.crypto.data.model.Article
import com.dreampany.framework.data.source.room.dao.BaseDao

/**
 * Created by roman on 9/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface ArticleDao : BaseDao<Article>  {
    @get:Query("select count(*) from article")
    val count: Int

    @get:Query("select * from article")
    val items: List<Article>?
}