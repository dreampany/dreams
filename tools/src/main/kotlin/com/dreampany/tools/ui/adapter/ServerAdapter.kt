package com.dreampany.tools.ui.adapter

import com.dreampany.framework.data.enums.Quality
import com.dreampany.framework.ui.adapter.SmartAdapter
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.model.ServerItem
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * Created by roman on 2019-10-12
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ServerAdapter(listener: Any? = null) : SmartAdapter<ServerItem>(listener) {

    private val qualityComparator: Comparator<IFlexible<*>>

    init {
        qualityComparator = QualityComparator()
    }

    companion object {
        private val SPAN_COUNT = 2
        private val ITEM_OFFSET = 4
    }

    var playingStationId: String = Constants.Default.STRING

    fun getSpanCount(): Int {
        return SPAN_COUNT
    }

    fun getItemOffset(): Int {
        return ITEM_OFFSET
    }

    override fun addItems(items: List<ServerItem>): Boolean {
        return super.addItems(items, qualityComparator)
    }

    override fun addItem(item: ServerItem): Boolean {
        return super.addItem(item, qualityComparator)
    }

    fun addFavoriteItems(items: List<ServerItem>): Boolean {
        for (item in items) {
            addFavoriteItem(item)
        }
        return true
    }

    fun addFavoriteItem(item: ServerItem) {
        if (item.favorite) {
            addItem(item, qualityComparator)
        } else {
            removeItem(item)
        }
    }

    class QualityComparator : Comparator<IFlexible<*>> {
        override fun compare(left: IFlexible<*>, right: IFlexible<*>): Int {
            val leftUiItem = left as ServerItem
            val rightUiItem = right as ServerItem
            val leftItem = leftUiItem.item
            val rightItem = rightUiItem.item
            return (rightItem.quality?.code ?: Quality.DEFAULT.code) - (leftItem.quality?.code ?: Quality.DEFAULT.code)
        }
    }
}