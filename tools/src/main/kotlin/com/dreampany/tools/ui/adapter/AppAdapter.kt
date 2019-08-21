package com.dreampany.tools.ui.adapter

import com.dreampany.frame.ui.adapter.SmartAdapter
import com.dreampany.tools.ui.model.AppItem

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class AppAdapter(listener: Any?) : SmartAdapter<AppItem>(listener) {

    //private val orderComparator: Comparator<IFlexible<*>>

    init {
      //  orderComparator = OrderComparator()
    }

    companion object {
        private val SPAN_COUNT = 3
        private val ITEM_OFFSET = 4
    }

    fun getSpanCount(): Int {
        return SPAN_COUNT
    }

    fun getItemOffset(): Int {
        return ITEM_OFFSET
    }

/*    fun setItems(items: List<AppItem>) {
        clear()
        addItems(0, items)
    }*/

/*    fun setItems(items: List<AppItem>) {
        super.setItems(items, orderComparator)
    }*/

/*    class OrderComparator : Comparator<IFlexible<*>> {
        override fun compare(left: IFlexible<*>, right: IFlexible<*>): Int {
            val leftItem = left as FeatureItem
            val rightItem = right as FeatureItem
            return (leftItem.order() - rightItem.order())
        }
    }*/

}