package com.dreampany.media.ui.adapter

import com.dreampany.frame.ui.adapter.SmartAdapter
import com.dreampany.frame.util.DataUtil
import com.dreampany.media.ui.model.MediaItem
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * Created by Roman-372 on 5/14/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
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

    fun addFavoriteItems(items: List<MediaItem>): Boolean {
        for (item in items) {
            addFavoriteItem(item)
        }
        return true
    }

    fun addFavoriteItem(item: MediaItem) {
        if (item.isFavorite) {
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