package com.dreampany.tools.data.misc

import android.content.Context
import com.dreampany.framework.data.enums.Level
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.misc.PointMapper
import com.dreampany.framework.data.model.Store
import com.dreampany.framework.data.source.api.PointDataSource
import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.tools.data.model.Quiz
import com.dreampany.tools.data.model.RelatedQuiz
import com.dreampany.tools.data.source.api.QuizDataSource
import com.dreampany.tools.misc.RelatedQuizAnnote
import com.dreampany.tools.misc.RelatedQuizItemAnnote
import com.dreampany.tools.ui.model.RelatedQuizItem
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class RelatedQuizMapper
@Inject constructor(
    private val context: Context,
    @RelatedQuizAnnote private val map: SmartMap<String, RelatedQuiz>,
    @RelatedQuizAnnote private val cache: SmartCache<String, RelatedQuiz>,
    @RelatedQuizItemAnnote private val uiMap: SmartMap<String, RelatedQuizItem>,
    @RelatedQuizItemAnnote private val uiCache: SmartCache<String, RelatedQuizItem>
) {

    fun isExists(item: RelatedQuiz): Boolean {
        return map.contains(item.id + item.type.name + item.subtype.name + item.level.name)
    }

    fun getUiItem(id: String, type: Type, subtype: Subtype, level: Level): RelatedQuizItem? {
        return uiMap.get(id + type.name + subtype.name + level.name)
    }

    fun putUiItem(uiItem: RelatedQuizItem) {
        val item = uiItem.item
        uiMap.put(item.id + item.type.name + item.subtype.name + item.level.name, uiItem)
    }

    fun getItem(
        id: String,
        type: Type,
        subtype: Subtype,
        level: Level,
        options: String
    ): RelatedQuiz? {
        var item = map.get(id)
        if (item == null) {
            item = RelatedQuiz(id = id)
            map.put(id, item)
        }
        item.type = type
        item.subtype = subtype
        item.level = level
        return item
    }

    fun getItem(input: Store, source: QuizDataSource): Quiz? {
        var out: RelatedQuiz? = map.get(input.id)
        if (out == null) {
            // out = source.getItem(input.id)
            map.put(input.id, out)
        }
        return null
    }

    fun getPoint(quiz: RelatedQuiz, pointMapper: PointMapper, source: PointDataSource): String? {
        if (quiz.answer.isNullOrEmpty() || quiz.given.isNullOrEmpty()) {
            return null
        }
        val credit = if (quiz.answer.equals(quiz.given))
            quiz.id.length + quiz.answer!!.length
        else -quiz.id.length
        val point = pointMapper.getItem(quiz.id, quiz.type, quiz.subtype, quiz.level, credit, source)
        return point?.id
    }
}