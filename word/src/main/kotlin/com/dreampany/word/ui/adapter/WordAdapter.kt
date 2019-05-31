package com.dreampany.word.ui.adapter

import com.dreampany.frame.ui.adapter.SmartAdapter
import com.dreampany.word.ui.model.WordItem
import eu.davidea.flexibleadapter.items.IFlexible


/**
 * Created by Hawladar Roman on 2/9/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
class WordAdapter(listener: Any) : SmartAdapter<WordItem>(listener) {

    private val recentComparator: Comparator<IFlexible<*>>
    private val alphaComparator: Comparator<IFlexible<*>>

    init {
        recentComparator = RecentComparator()
        alphaComparator = AlphaComparator()
    }

    fun addItemsByRecent(items: List<WordItem>): Boolean {
        if (isEmpty) {
            return super.addItems(items)
        }
        for (item in items) {
            addItem(item, recentComparator)
        }
        return true
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

    class RecentComparator : Comparator<IFlexible<*>> {
        override fun compare(p0: IFlexible<*>?, p1: IFlexible<*>?): Int {
            val left = p0 as WordItem
            val right = p1 as WordItem
            return (right.time - left.time).toInt()
        }
    }

    class AlphaComparator : Comparator<IFlexible<*>> {
        override fun compare(p0: IFlexible<*>?, p1: IFlexible<*>?): Int {
            val left = p0 as WordItem
            val right = p1 as WordItem
            val leftItem = left.item
            val rightItem = right.item
            return leftItem.word.compareTo(rightItem.word)
        }
    }
}