package com.dreampany.tools.data.misc

import android.content.Context
import com.dreampany.frame.data.enums.Level
import com.dreampany.frame.data.enums.Subtype
import com.dreampany.frame.data.enums.Type
import com.dreampany.frame.data.model.Store
import com.dreampany.frame.misc.SmartCache
import com.dreampany.frame.misc.SmartMap
import com.dreampany.tools.data.model.Quiz
import com.dreampany.tools.data.source.api.QuizDataSource
import com.dreampany.tools.misc.QuizAnnote
import com.dreampany.tools.misc.QuizItemAnnote
import com.dreampany.tools.ui.model.QuizItem
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class QuizMapper
@Inject constructor(
    private val context: Context,
    @QuizAnnote private val map: SmartMap<String, Quiz>,
    @QuizAnnote private val cache: SmartCache<String, Quiz>,
    @QuizItemAnnote private val uiMap: SmartMap<String, QuizItem>,
    @QuizItemAnnote private val uiCache: SmartCache<String, QuizItem>
) {

    fun isExists(item: Quiz): Boolean {
        return map.contains(item.id)
    }

    fun getUiItem(id: String): QuizItem? {
        return uiMap.get(id)
    }

    fun putUiItem(id: String, uiItem: QuizItem) {
        uiMap.put(id, uiItem)
    }

    fun getItem(id: String, type: Type, subtype: Subtype, level: Level, title: String): Quiz? {
        var item = map.get(id)
        if (item == null) {
            item = Quiz(id = id)
            map.put(id, item)
        }
        item.type = type
        item.subtype = subtype
        item.level = level
        item.title = title
        return item
    }

    fun getItem(input: Store, source: QuizDataSource): Quiz? {
        var out: Quiz? = map.get(input.id)
        if (out == null) {
            out = source.getItem(input.id)
            map.put(input.id, out)
        }
        return out
    }
}