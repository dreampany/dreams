package com.dreampany.tools.ui.adapter

import com.dreampany.framework.ui.adapter.SmartAdapter
import com.dreampany.tools.ui.model.StationItem
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * Created by roman on 2019-10-12
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class StationAdapter (listener: Any? = null) : SmartAdapter<StationItem>(listener) {

    private val clickCountComparator: Comparator<IFlexible<*>>

    init {
        clickCountComparator = ClickCountComparator()
    }

    companion object {
        private val SPAN_COUNT = 2
        private val ITEM_OFFSET = 4
    }

    fun getSpanCount(): Int {
        return StationAdapter.SPAN_COUNT
    }

    fun getItemOffset(): Int {
        return StationAdapter.ITEM_OFFSET
    }

    override fun addItems(items: List<StationItem>): Boolean {
        return super.addItems(items, clickCountComparator)
    }

    override fun addItem(item: StationItem): Boolean {
        return super.addItem(item, clickCountComparator)
    }

    fun addFavoriteItems(items: List<StationItem>): Boolean {
        for (item in items) {
            addFavoriteItem(item)
        }
        return true
    }

    fun addFavoriteItem(item: StationItem) {
        if (item.favorite) {
            addItem(item, clickCountComparator)
        } else {
            removeItem(item)
        }
    }

    class ClickCountComparator : Comparator<IFlexible<*>> {
        override fun compare(left: IFlexible<*>, right: IFlexible<*>): Int {
            val leftUiItem = left as StationItem
            val rightUiItem = right as StationItem
            val leftItem = leftUiItem.item
            val rightItem = rightUiItem.item
            return rightItem.getClickCount() - leftItem.getClickCount()
        }
    }
}