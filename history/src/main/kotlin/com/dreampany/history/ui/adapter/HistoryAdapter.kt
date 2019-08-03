package com.dreampany.history.ui.adapter

import com.dreampany.frame.ui.adapter.SmartAdapter
import com.dreampany.history.ui.model.HistoryItem
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * Created by roman on 2019-07-25
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class HistoryAdapter(listener: Any?) : SmartAdapter<HistoryItem>(listener) {

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

    fun setItems(items: List<HistoryItem>) {
        super.setItems(items, recentComparator)
    }

    fun addFavoriteItem(item: HistoryItem) {
        if (item.favorite) {
            addItem(item)
        } else {
            removeItem(item)
        }
    }

    fun addFavoriteItems(items: List<HistoryItem>): Boolean {
        for (item in items) {
            addFavoriteItem(item)
        }
        return true
    }

    class RecentComparator : Comparator<IFlexible<*>> {
        override fun compare(left: IFlexible<*>, right: IFlexible<*>): Int {
            val leftItem = left as HistoryItem
            val rightItem = right as HistoryItem
            return (rightItem.item.year - leftItem.item.year)
        }
    }
}