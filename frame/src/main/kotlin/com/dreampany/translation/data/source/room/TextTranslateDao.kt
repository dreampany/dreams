package com.dreampany.translation.data.source.room

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.frame.data.source.BaseDaoKt
import com.dreampany.translation.data.model.TextTranslation

/**
 * Created by Roman-372 on 7/4/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface TextTranslateDao : BaseDaoKt<TextTranslation> {

    @Query("select * from texttranslation where input = :input and source = :source and target = :target limit 1")
    fun getItem(input: String, source: String, target: String): TextTranslation
}