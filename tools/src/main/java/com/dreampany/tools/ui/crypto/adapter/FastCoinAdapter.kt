package com.dreampany.tools.ui.crypto.adapter

import android.os.Bundle
import androidx.core.app.BundleCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.common.data.enums.Order
import com.dreampany.common.misc.extension.dimension
import com.dreampany.common.ui.misc.ItemSpaceDecoration
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.CoinSort
import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.misc.CurrencyFormatter
import com.dreampany.tools.ui.crypto.model.CoinItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ModelAdapter

/**
 * Created by roman on 13/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class FastCoinAdapter {

    private var fastAdapter: FastAdapter<CoinItem>? = null
    private var itemAdapter: ModelAdapter<Coin, CoinItem>? = null

    fun initRecycler(
        state: Bundle?,
        recycler: RecyclerView,
        formatter: CurrencyFormatter,
        currency: Currency,
        sort: CoinSort,
        order: Order
    ) {
        itemAdapter = ModelAdapter { element: Coin ->
            CoinItem(
                element,
                formatter,
                currency,
                sort,
                order
            )
        }
        itemAdapter?.itemFilter?.filterPredicate = { item: CoinItem, constraint: CharSequence? ->
            item.coin.name.toString().contains(constraint.toString(), ignoreCase = true)
        }

        itemAdapter?.let {
            fastAdapter = FastAdapter.with(listOf(it))
        }

        recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = fastAdapter
            addItemDecoration(
                ItemSpaceDecoration(
                    context.dimension(R.dimen.recycler_horizontal_spacing).toInt(),
                    context.dimension(R.dimen.recycler_vertical_spacing).toInt(),
                    1,
                    true
                )
            )
        }
        fastAdapter?.withSavedInstanceState(state)
    }

    fun destroy() {
        //fastAdapter?.
        fastAdapter = null
        itemAdapter = null
    }

    fun saveInstanceState(outState: Bundle): Bundle {
        return fastAdapter?.saveInstanceState(outState) ?: outState
    }

    fun filter(constraint: CharSequence?) {
        itemAdapter?.filter(constraint)
    }

    fun addItems(coins : List<Coin>) {
        itemAdapter?.add(coins)
    }

    val itemCount: Long
        get() = (fastAdapter?.itemCount ?: 0).toLong()
}