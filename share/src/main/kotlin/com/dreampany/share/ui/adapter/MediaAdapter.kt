package com.dreampany.share.ui.adapter

import com.dreampany.frame.ui.adapter.SmartAdapter
import com.dreampany.frame.util.DataUtil
import com.dreampany.share.ui.model.MediaItem
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * Created by Hawladar Roman on 7/17/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
class MediaAdapter(listener: Any) : SmartAdapter<MediaItem>(listener) {
    private val dateModifiedComparator: Comparator<IFlexible<*>>

    init {
        dateModifiedComparator = DateModifiedComparator()
    }

    override fun addItems(items: List<MediaItem>): Boolean {
        if (DataUtil.isEmpty(items)) {
            return false
        }
        if (isEmpty) {
            addItems(0, items)
        } else {
            for (item in items) {
                addItem(item, dateModifiedComparator)
            }
        }
        return true
    }

    fun addFlagItems(items: List<MediaItem>): Boolean {
        for (item in items) {
            addFlagItem(item)
        }
        return true
    }

    fun addFlagItem(item: MediaItem) {
        if (item.isFlagged) {
            addItem(item, dateModifiedComparator)
        } else {
            removeItem(item)
        }
    }

    fun addShareItems(items: List<MediaItem>): Boolean {
        for (item in items) {
            addShareItem(item)
        }
        return true
    }

    fun addShareItem(item: MediaItem) {
        if (item.isShared) {
            addItem(item, dateModifiedComparator)
        } else {
            removeItem(item)
        }
    }

    class DateModifiedComparator : Comparator<IFlexible<*>> {
        override fun compare(p0: IFlexible<*>?, p1: IFlexible<*>?): Int {
            val left = p0 as MediaItem
            val right = p1 as MediaItem
            val leftItem = left.getItem()
            val rightItem = right.getItem()
            return (rightItem.dateModified - leftItem.dateModified).toInt()
        }
    }
}
