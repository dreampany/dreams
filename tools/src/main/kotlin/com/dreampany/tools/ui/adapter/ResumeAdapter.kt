package com.dreampany.tools.ui.adapter

import com.dreampany.framework.ui.adapter.SmartAdapter
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.model.ResumeItem
import com.dreampany.tools.ui.model.ServerItem
import com.dreampany.tools.ui.model.WordItem
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * Created by roman on 2020-01-12
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ResumeAdapter(listener: Any? = null) : SmartAdapter<ResumeItem>(listener) {

    private val recentComparator: Comparator<IFlexible<*>>

    init {
        recentComparator = RecentComparator()
    }

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

    class RecentComparator : Comparator<IFlexible<*>> {
        override fun compare(p0: IFlexible<*>?, p1: IFlexible<*>?): Int {
            val left = p0 as ResumeItem
            val right = p1 as ResumeItem
            return (left.time - right.time).toInt()
        }
    }
}