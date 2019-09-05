package com.dreampany.tools.ui.adapter

import com.dreampany.framework.ui.adapter.SmartAdapter
import com.dreampany.tools.ui.model.MoreItem


/**
 * Created by Hawladar Roman on 5/31/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
class MoreAdapter(listener: Any) : SmartAdapter<MoreItem>(listener) {

    companion object {
        private val SPAN_COUNT = 3
        private val ITEM_OFFSET_EMPTY = 0
        private val ITEM_OFFSET = 1
    }

    fun getSpanCount(): Int {
        return SPAN_COUNT
    }

    fun getItemOffsetEmpty(): Int {
        return ITEM_OFFSET_EMPTY
    }

    fun getItemOffset(): Int {
        return ITEM_OFFSET
    }

}