package com.dreampany.tools.ui.adapter

import com.dreampany.framework.ui.adapter.SmartAdapter
import com.dreampany.tools.ui.model.NoteItem
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class NoteAdapter(listener: Any? = null) : SmartAdapter<NoteItem>(listener) {

    private val timeComparator: Comparator<IFlexible<*>>

    init {
        timeComparator = TimeComparator()
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

    override fun addItems(items: List<NoteItem>): Boolean {
        return super.addItems(items, timeComparator)
    }

    override fun addItem(item: NoteItem): Boolean {
        return super.addItem(item, timeComparator)
    }

    fun addFavoriteItems(items: List<NoteItem>): Boolean {
        for (item in items) {
            addFavoriteItem(item)
        }
        return true
    }

    fun addFavoriteItem(item: NoteItem) {
        if (item.favorite) {
            addItem(item, timeComparator)
        } else {
            removeItem(item)
        }
    }

    class TimeComparator : Comparator<IFlexible<*>> {
        override fun compare(left: IFlexible<*>, right: IFlexible<*>): Int {
            val leftUiItem = left as NoteItem
            val rightUiItem = right as NoteItem
            val leftItem = leftUiItem.item
            val rightItem = rightUiItem.item
            return (rightItem.time - leftItem.time).toInt()
        }
    }
}