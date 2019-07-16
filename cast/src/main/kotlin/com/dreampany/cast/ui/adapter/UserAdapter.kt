package com.dreampany.cast.ui.adapter

import com.dreampany.cast.ui.model.UserItem
import com.dreampany.frame.ui.adapter.SmartAdapter
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * Created by Roman-372 on 6/27/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class UserAdapter(listener: Any) : SmartAdapter<UserItem>(listener) {

    class RecentComparator : java.util.Comparator<IFlexible<*>> {
        override fun compare(left: IFlexible<*>?, right: IFlexible<*>?): Int {
            val leftItem = left as UserItem
            val rightItem = right as UserItem
            return (rightItem.item.time - leftItem.item.time).toInt()
        }
    }

    private val recentComparator: Comparator<IFlexible<*>>

    init {
        recentComparator = RecentComparator()
    }
}