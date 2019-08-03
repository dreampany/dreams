package com.dreampany.lca.ui.adapter

import com.dreampany.frame.ui.adapter.SmartAdapter
import com.dreampany.frame.util.DataUtil
import com.dreampany.lca.data.enums.Currency
import com.dreampany.lca.ui.model.CoinItem
import eu.davidea.flexibleadapter.items.IFlexible


/**
 * Created by Hawladar Roman on 5/31/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
class CoinAdapter(listener: Any) : SmartAdapter<CoinItem>(listener) {

    private val rankComparator: Comparator<IFlexible<*>> //it can be multiple comparator to support multiple sorting
    private var currency : Currency? = null

    init {
        rankComparator = RankComparator()
    }

    override fun addItems(items: List<CoinItem>): Boolean {
        if (isEmpty) {
            return super.addItems(items);
        }
        for (item in items) {
            addItem(item, rankComparator)
        }
        return true
    }

    fun updateCurrency(currency: Currency) {
        val items = currentItems
        for (item in items) {
            item.currency = currency
        }
        updateSilently(items)
    }

     fun loadMoreComplete(items: List<CoinItem>?) {
        if (items == null || items.isEmpty()) {
            super.onLoadMoreComplete(null)
        } else {
            val updates = ArrayList<CoinItem>()
            for (item in items) {
                if (contains(item)) {
                    updates.add(item)
                }
            }
            DataUtil.removeAll(items, updates)
            addItems(updates)
            super.onLoadMoreComplete(items, 5000)
        }
    }

    fun addFavoriteItems(items: List<CoinItem>): Boolean {
        for (item in items) {
            addFavoriteItem(item)
        }
        return true
    }

    fun addFavoriteItem(item: CoinItem) {
        if (item.favorite) {
            addItem(item, rankComparator)
        } else {
            removeItem(item)
        }
    }

    class RankComparator : Comparator<IFlexible<*>> {
        override fun compare(leftChild: IFlexible<*>?, rightChild: IFlexible<*>?): Int {
            val left = leftChild as CoinItem
            val right = rightChild as CoinItem
            val leftItem = left.item
            val rightItem = right.item
            return leftItem.rank - rightItem.rank
        }
    }
}