package com.dreampany.tools.ui.adapter.question

import com.dreampany.framework.ui.adapter.SmartAdapter
import com.dreampany.tools.ui.model.question.QuestionItem

/**
 * Created by roman on 2020-02-22
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class QuestionAdapter(listener: Any? = null) : SmartAdapter<QuestionItem>(listener) {

    companion object {
        private val SPAN_COUNT = 2
        private val ITEM_OFFSET = 4
    }


    fun getSpanCount(): Int {
        return SPAN_COUNT
    }

    fun getItemOffset(): Int {
        return ITEM_OFFSET
    }

    fun getPoints() : Long {
        var points = 0L
        currentItems.forEach {
            points += it.points
        }
        return points
    }
}