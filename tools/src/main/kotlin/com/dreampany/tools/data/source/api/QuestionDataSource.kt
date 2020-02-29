package com.dreampany.tools.data.source.api

import com.dreampany.framework.data.enums.Difficult
import com.dreampany.framework.data.source.api.DataSource
import com.dreampany.framework.data.source.api.DataSourceRx
import com.dreampany.tools.data.model.question.Question
import io.reactivex.Maybe

/**
 * Created by roman on 2020-02-13
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface QuestionDataSource : DataSource<Question>, DataSourceRx<Question> {
    fun getItems(
        category: Question.Category?,
        type: Question.Type?,
        difficult: Difficult?,
        limit: Long
    ): List<Question>?

    fun getItemsRx(
        category: Question.Category?,
        type: Question.Type?,
        difficult: Difficult?,
        limit: Long
    ): Maybe<List<Question>>
}