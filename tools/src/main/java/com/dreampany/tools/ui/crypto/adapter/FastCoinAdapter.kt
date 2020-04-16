package com.dreampany.tools.ui.crypto.adapter

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.common.misc.extension.dimension
import com.dreampany.common.ui.misc.ItemSpaceDecoration
import com.dreampany.tools.R
import com.dreampany.tools.ui.crypto.model.CoinItem
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.mikepenz.fastadapter.adapters.GenericFastItemAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.scroll.EndlessRecyclerOnScrollListener
import com.mikepenz.fastadapter.ui.items.ProgressItem
import com.mikepenz.fastadapter.utils.ComparableItemListImpl

/**
 * Created by roman on 13/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class FastCoinAdapter(
    val scrollListener : ((currentPage: Int) -> Unit)? = null
) {

    private lateinit var scroller: EndlessRecyclerOnScrollListener
    private lateinit var fastAdapter: GenericFastItemAdapter
    private lateinit var itemAdapter: GenericItemAdapter
    private lateinit var footerAdapter: GenericItemAdapter

    private val rankComparator: Comparator<GenericItem>

    init {
        rankComparator = RankComparator()
    }

    fun initRecycler(
        state: Bundle?,
        recycler: RecyclerView
    ) {
        val list = ComparableItemListImpl(comparator = rankComparator)
        itemAdapter = ItemAdapter(list)
        itemAdapter.itemFilter.filterPredicate = { item: GenericItem, constraint: CharSequence? ->
            if (item is CoinItem)
                item.coin.name.toString().contains(constraint.toString(), ignoreCase = true)
            else
                false
        }
        footerAdapter = ItemAdapter.items()
        fastAdapter = FastItemAdapter(itemAdapter)
        fastAdapter.addAdapter(1, footerAdapter)

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

            scrollListener?.let {
                scroller = object : EndlessRecyclerOnScrollListener(footerAdapter) {
                    override fun onLoadMore(currentPage: Int) {
                        it(currentPage)
                    }
                }
                addOnScrollListener(scroller)
            }
        }
        fastAdapter.withSavedInstanceState(state)
    }

    fun destroy() {
    }

    fun saveInstanceState(outState: Bundle): Bundle {
        return fastAdapter.saveInstanceState(outState) ?: outState
    }

    fun filter(constraint: CharSequence?) {
        fastAdapter.filter(constraint)
    }

    fun showScrollProgress() {
        footerAdapter.clear()
        val progressItem = ProgressItem()
        progressItem.isEnabled = false
        footerAdapter.add(progressItem)
    }

    fun hideScrollProgress() {
        footerAdapter.clear()
    }

    fun addItems(items: List<CoinItem>) {
        fastAdapter.add(items)
    }


    val itemCount: Long
        get() = (fastAdapter.itemCount ?: 0).toLong()

    class RankComparator : Comparator<GenericItem> {
        override fun compare(left: GenericItem, right: GenericItem): Int {
            if (left is CoinItem && right is CoinItem) {
                return left.coin.rank - right.coin.rank
            }
            return 0
        }
    }
}