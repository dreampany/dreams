package com.dreampany.tools.data.source.news.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.framework.data.source.room.dao.BaseDao
import com.dreampany.tools.data.model.news.Article

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