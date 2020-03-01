package com.dreampany.tools.data.mapper

import android.content.Context
import com.dreampany.framework.data.enums.Level
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.misc.PointMapper
import com.dreampany.framework.data.model.Point
import com.dreampany.framework.data.source.api.PointDataSource
import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.framework.misc.extensions.hash512
import com.dreampany.tools.api.question.model.TriviaQuestion
import com.dreampany.tools.data.model.question.Question
import com.dreampany.tools.injector.annote.question.QuestionAnnote
import com.dreampany.tools.injector.annote.question.QuestionItemAnnote
import com.dreampany.tools.misc.Constants
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
) {

    fun getUiItem(id: String): QuestionItem? {
        return uiMap.get(id)
    }

    fun putUiItem(id: String, uiItem: QuestionItem) {
        uiMap.put(id, uiItem)
    }

    fun getItems(inputs: List<TriviaQuestion>?): List<Question>? {
        if (inputs.isNullOrEmpty()) return null
        val result = arrayListOf<Question>()
        inputs.forEach { rs ->
            result.add(getItem(rs))
        }
        return result
    }

    fun getItem(input: TriviaQuestion): Question {
        val id = input.question.hash512()
        var out: Question? = map.get(id)
        if (out == null) {
            out = Question(id)
            map.put(id, out)
            //if (!input.options.contains(input.answer))
            input.options.add(input.answer)
            input.options.sort()
        }
        out.apply {
            category = Constants.Values.QuestionValues.getCategory(input.category)
            type = Constants.Values.QuestionValues.getType(input.type)
            difficult = Constants.Values.QuestionValues.getDifficult(input.difficulty)
            question = input.question
            answer = input.answer
            options = input.options
        }

        return out
    }

    fun getPoint(question: Question, given: String?, pointMapper: PointMapper, source: PointDataSource): Point? {
        if (question.answer.isNullOrEmpty() || given.isNullOrEmpty()) {
            return null
        }
        val points = if (question.answer.equals(given))  Constants.Points.QUESTION
        else 0L
        val point = pointMapper.getItem(question.id, Type.QUESTION, Subtype.DEFAULT, Level.A1, points, given, source)
        return point
    }

}