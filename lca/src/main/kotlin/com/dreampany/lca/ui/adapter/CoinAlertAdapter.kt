package com.dreampany.lca.ui.adapter

import com.dreampany.frame.ui.adapter.SmartAdapter
import com.dreampany.lca.ui.model.CoinAlertItem
import eu.davidea.flexibleadapter.items.IFlexible


/**
 * Created by Hawladar Roman on 5/31/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
class CoinAlertAdapter(listener: Any) : SmartAdapter<CoinAlertItem>(listener) {

    private val recentComparator: Comparator<IFlexible<*>> //it can be multiple comparator to support multiple sorting
    private var deleting : Boolean = false

    init {
        recentComparator = RecentComparator()
    }

    override fun addItems(items: List<CoinAlertItem>): Boolean {
        if (isEmpty) {
            return super.addItems(items);
        }
        for (item in items) {
            addItem(item, recentComparator)
        }
        return true
    }

    fun toggleDelete() {
        deleting = !deleting
        for (item in currentItems) {
            item.deleting = deleting
        }
        notifyDataSetChanged()
    }

    class RecentComparator : Comparator<IFlexible<*>> {
        override fun compare(p0: IFlexible<*>?, p1: IFlexible<*>?): Int {
            val left = p0 as CoinAlertItem
            val right = p1 as CoinAlertItem
            val leftItem = left.item
            val rightItem = right.item
            return (leftItem.time - rightItem.time).toInt()
        }
    }
}