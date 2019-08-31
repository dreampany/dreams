package com.dreampany.translation.data.source.room

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.frame.data.source.room.dao.BaseDao
import com.dreampany.translation.data.model.TextTranslation
import io.reactivex.Maybe

/**
 * Created by Roman-372 on 7/4/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface TextTranslationDao : BaseDao<TextTranslation> {

    @Query("select count(*) from texttranslation where source = :source and target = :target and input = :input limit 1")
    fun getCount(source: String, target: String, input: String): Int

    @Query("select count(*) from texttranslation where source = :source and target = :target and input = :input limit 1")
    fun getCountRx(source: String, target: String, input: String): Maybe<Int>

    @Query("select * from texttranslation where source = :source and target = :target and input = :input limit 1")
    fun getItem(source: String, target: String, input: String): TextTranslation?

}