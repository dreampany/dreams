package com.dreampany.tools.ui.adapter.crypto

import com.dreampany.framework.ui.adapter.SmartAdapter
import com.dreampany.tools.data.enums.CoinSort
import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.data.enums.Order
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.model.crypto.CoinItem
import eu.davidea.flexibleadapter.items.IFlexible
import java.util.Comparator

/**
 * Created by roman on 2019-11-15
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CoinAdapter (listener: Any? = null) : SmartAdapter<CoinItem>(listener) {

    companion object {
        private val SPAN_COUNT = 2
        private val ITEM_OFFSET = 4
    }

    fun getSpanCount(): Int {
        return CoinAdapter.SPAN_COUNT
    }

    fun getItemOffset(): Int {
        return CoinAdapter.ITEM_OFFSET
    }

    fun addItems(currency: Currency, sort: CoinSort, order: Order, items: List<CoinItem>): Boolean {
        val comparator: Comparator<IFlexible<*>> = Constants.Comparators.Crypto.getUiComparator(currency, sort, order)
        as Comparator<IFlexible<*>>
        return super.addItems(items, comparator)
    }
}