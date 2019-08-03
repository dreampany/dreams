package com.dreampany.tools.ui.adapter

import com.dreampany.frame.ui.adapter.SmartAdapter
import com.dreampany.tools.ui.model.FeatureItem
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class FeatureAdapter(listener: Any?) : SmartAdapter<FeatureItem>(listener) {

    private val orderComparator: Comparator<IFlexible<*>>

    init {
        orderComparator = OrderComparator()
    }

    fun setItems(items: List<FeatureItem>) {
        super.setItems(items, orderComparator)
    }

    class OrderComparator : Comparator<IFlexible<*>> {
        override fun compare(left: IFlexible<*>, right: IFlexible<*>): Int {
            val leftItem = left as FeatureItem
            val rightItem = right as FeatureItem
            return (leftItem.order() - rightItem.order())
        }
    }

}