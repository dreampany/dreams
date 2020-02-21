package com.dreampany.tools.data.mapper

import android.content.Context
import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.tools.data.model.question.Question
import com.dreampany.tools.injector.annote.question.QuestionAnnote
import com.dreampany.tools.injector.annote.question.QuestionItemAnnote
import com.dreampany.tools.ui.model.question.QuestionItem
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2020-02-16
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class QuestionMapper
@Inject constructor(
    private val context: Context,
    @QuestionAnnote private val map: SmartMap<String, Question>,
    @QuestionAnnote private val cache: SmartCache<String, Question>,
    @QuestionItemAnnote private val uiMap: SmartMap<String, QuestionItem>,
    @QuestionItemAnnote private val uiCache: SmartCache<String, QuestionItem>
){

    fun getUiItem(id: String): QuestionItem? {
        return uiMap.get(id)
    }

    fun putUiItem(id: String, uiItem: QuestionItem) {
        uiMap.put(id, uiItem)
    }
}