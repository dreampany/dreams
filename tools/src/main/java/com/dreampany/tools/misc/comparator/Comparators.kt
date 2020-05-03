package com.dreampany.tools.misc.comparator

import com.dreampany.framework.data.enums.Order
import com.dreampany.tools.data.enums.crypto.CoinSort
import com.dreampany.tools.data.enums.crypto.Currency
import com.dreampany.tools.data.model.crypto.Coin
import com.google.common.collect.Maps

/**
 * Created by roman on 3/22/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Comparators {
    object Crypto {
        private val comparators: HashMap<Pair<CoinSort, Order>, Comparator<Coin>> =
            Maps.newHashMap()

        fun getComparator(
            currency: Currency,
            sort: CoinSort,
            order: Order
        ): Comparator<Coin> {
            val pair = Pair(sort, order)
            if (!comparators.containsKey(pair)) {
                //comparators.put(pair, createComparator(currency, sort, order))
            }
            return comparators.get(pair)!!
        }

        /*private fun createComparator(
            currency: Currency,
            sort: CoinSort,
            order: Order
        ): Comparator<Coin> {
            when (sort) {
                CoinSort.MARKET_CAP,
                CoinSort.RANK -> {
                    return object : Comparator<Coin> {
                        override fun compare(
                            left: Coin, right: Coin
                        ): Int {
                            val leftQuote = left.getQuote(currency)
                            val rightQuote = right.getQuote(currency)

                            val leftCap =
                                if (leftQuote != null) leftQuote.getMarketCap() else Constants.Default.DOUBLE
                            val rightCap =
                                if (rightQuote != null) rightQuote.getMarketCap() else Constants.Default.DOUBLE

                            when (order) {
                                Order.ASCENDING -> {
                                    return leftCap.compareTo(rightCap)
                                }
                                Order.DESCENDING -> {
                                    return rightCap.compareTo(leftCap)
                                }
                            }
                        }

                    }
                }
            }
        }*/
    }
}