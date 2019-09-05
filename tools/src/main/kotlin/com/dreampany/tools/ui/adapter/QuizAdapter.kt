package com.dreampany.tools.ui.adapter

import com.dreampany.framework.ui.adapter.SmartAdapter
import com.dreampany.tools.ui.model.QuizItem


/**
 * Created by Hawladar Roman on 2/9/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
class QuizAdapter(listener: Any?) : SmartAdapter<QuizItem>(listener) {

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
}