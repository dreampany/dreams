package com.dreampany.tools.ui.adapter

import com.dreampany.framework.ui.adapter.SmartAdapter
import com.dreampany.tools.ui.model.WordItem
import eu.davidea.flexibleadapter.items.IFlexible


/**
 * Created by Hawladar Roman on 2/9/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
class MessageAdapter(listener: Any? = null) : SmartAdapter<WordItem>(listener) {

    private val recentComparator: Comparator<IFlexible<*>>
    private val alphaComparator: Comparator<IFlexible<*>>

    init {
        recentComparator = RecentComparator()
        alphaComparator = AlphaComparator()
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

    fun addItemsOfHistory(items: List<WordItem>): Boolean {
        if (isEmpty) {
            return super.addItems(items)
        }
        for (item in items) {
            addItem(item, recentComparator)
        }
        return true
    }

    fun addItemOfHistory(item: WordItem): Boolean {
        return addItem(item, recentComparator)
    }

    fun addItemsByAlpha(items: List<WordItem>): Boolean {
        if (isEmpty) {
            return super.addItems(items)
        }
        for (item in items) {
            addItem(item, alphaComparator)
        }
        return true
    }

    fun addItemsBySearch(items: List<WordItem>): Boolean {
        if (!isEmpty) {
            clear()
        }
        for (item in items) {
            addItem(item, alphaComparator)
        }
        return super.addItems(items, alphaComparator)
    }

    fun addFavoriteItem(item: WordItem) {
        if (item.favorite) {
            addItem(item)
        } else {
            removeItem(item)
        }
    }

    fun addFavoriteItems(items: List<WordItem>): Boolean {
        for (item in items) {
            addFavoriteItem(item)
        }
        return true
    }

    class RecentComparator : Comparator<IFlexible<*>> {
        override fun compare(p0: IFlexible<*>?, p1: IFlexible<*>?): Int {
            val left = p0 as WordItem
            val right = p1 as WordItem
            return (left.time - right.time).toInt()
        }
    }

    class AlphaComparator : Comparator<IFlexible<*>> {
        override fun compare(p0: IFlexible<*>?, p1: IFlexible<*>?): Int {
            val left = p0 as WordItem
            val right = p1 as WordItem
            val leftItem = left.item
            val rightItem = right.item
            return leftItem.id.compareTo(rightItem.id)
        }
    }
}