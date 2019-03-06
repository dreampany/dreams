package com.dreampany.lca.ui.adapter

import com.dreampany.frame.ui.adapter.SmartAdapter
import com.dreampany.frame.util.DataUtil
import com.dreampany.lca.data.model.Coin
import com.dreampany.lca.data.model.Currency
import com.dreampany.lca.ui.model.CoinItem
import eu.davidea.flexibleadapter.items.IFlexible


/**
 * Created by Hawladar Roman on 5/31/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
class CoinAdapter(listener: Any) : SmartAdapter<CoinItem>(listener) {

    private val rankComparator: Comparator<IFlexible<*>> //it can be multiple comparator to support multiple sorting

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

    fun addFlagItems(items: List<CoinItem>): Boolean {
        for (item in items) {
            addFlagItem(item)
        }
        return true
    }

    fun addFlagItem(item: CoinItem) {
        if (item.isFavorite) {
            addItem(item, rankComparator)
        } else {
            removeItem(item)
        }
    }

    fun load(coin: Coin) {
        addItem(CoinItem.getDetailsItem(coin))
        addItem(CoinItem.getQuoteItem(coin, Currency.USD))
        addItem(CoinItem.getQuoteItem(coin, Currency.EUR))
    }

    class RankComparator : Comparator<IFlexible<*>> {
        override fun compare(p0: IFlexible<*>?, p1: IFlexible<*>?): Int {
            val left = p0 as CoinItem
            val right = p1 as CoinItem
            val leftItem = left.item
            val rightItem = right.item
            return leftItem.rank - rightItem.rank
        }
    }
}