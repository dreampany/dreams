package com.dreampany.tools.ui.wifi.adapter

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.adapter.SpacingItemDecoration
import com.dreampany.framework.misc.exts.dimension
import com.dreampany.framework.misc.exts.value
import com.dreampany.tools.R
import com.dreampany.tools.ui.wifi.model.WifiItem
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.mikepenz.fastadapter.adapters.GenericFastItemAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.scroll.EndlessRecyclerOnScrollListener
import com.mikepenz.fastadapter.ui.items.ProgressItem
import com.mikepenz.fastadapter.utils.ComparableItemListImpl
import org.apache.commons.lang3.builder.CompareToBuilder

/**
 * Created by roman on 13/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class FastWifiAdapter(
    val scrollListener: ((currentPage: Int) -> Unit)? = null,
    val clickListener: ((view: View, item: WifiItem) -> Unit)? = null
) {

    private lateinit var scroller: EndlessRecyclerOnScrollListener
    private lateinit var fastAdapter: GenericFastItemAdapter
    private lateinit var itemAdapter: GenericItemAdapter
    private lateinit var footerAdapter: GenericItemAdapter

    private lateinit var timeBssidComparator: Comparator<GenericItem>
    private lateinit var bssidComparator: Comparator<GenericItem>
    //private val rankComparator: Comparator<GenericItem>

    init {
        //rankComparator = RankComparator()
    }

    val itemCount: Int
        get() = fastAdapter.adapterItems.size

    val isEmpty: Boolean get() = itemCount == 0

    fun initRecycler(
        state: Bundle?,
        recycler: RecyclerView
    ) {
        timeBssidComparator = TimeBssidComparator()
        val list = ComparableItemListImpl(comparator = timeBssidComparator)
        itemAdapter = ItemAdapter(list)
        itemAdapter.itemFilter.filterPredicate = { item: GenericItem, constraint: CharSequence? ->
            if (item is WifiItem)
                item.input.ssid.contains(constraint.toString(), ignoreCase = true)
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
                SpacingItemDecoration(
                    1,
                    context.dimension(R.dimen.recycler_vertical_spacing).toInt(),
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

        clickListener?.let { listener ->
            /*fastAdapter.onClickListener = { view, adapter, item, position ->
                if (item is CoinItem)
                    view?.let {
                        listener(it, item)
                    }
                false
            }*/
            /*fastAdapter.addClickListener<CoinItemBinding, GenericItem>(
                { bind -> bind.root }, { bind -> arrayListOf(bind.layoutOptions.buttonFavorite) }
            )
            { view, position, adapter, item ->
                if (item is CoinItem) {
                    listener(view, item)
                }
            }*/

            /*fastAdapter.addClickListener<CoinInfoItemBinding, GenericItem>(
                { bind -> bind.root },
                { bind -> arrayListOf(bind.buttonFavorite) }
            )
            { view, position, adapter, item ->
                if (item is CoinItem) {
                    listener(view, item)
                }
            }*/
        }
    }

    fun destroy() {
    }

    fun saveInstanceState(outState: Bundle): Bundle {
        return fastAdapter.saveInstanceState(outState)
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

    fun updateItem(item: WifiItem): Boolean {
        val position = findPosition(item)
        if (position >= 0) {
            fastAdapter.set(position, item)
            return true
            //fastAdapter.notifyAdapterItemChanged(position)
        }
        return false
    }

    fun updateItems(items: List<WifiItem>) {
        items.forEach {
            updateItem(it)
        }
    }

    fun addItem(item: WifiItem) {
        val position = findPosition(item)
        if (position >= 0) {
            fastAdapter.remove(position)
        }
        fastAdapter.add(item)
    }

    fun addItems(items: List<WifiItem>) {
        items.forEach { addItem(it) }
    }

    private fun findPosition(item: WifiItem): Int {
        var position = fastAdapter.getAdapterPosition(item)
        position = fastAdapter.getGlobalPosition(position)
        return position
    }

    class TimeBssidComparator : Comparator<GenericItem> {
        override fun compare(left: GenericItem, right: GenericItem): Int {
            if (left is WifiItem && right is WifiItem) {
                /*return CompareToBuilder()
                    .append(right.input.signal?.level, left.input.signal?.level)
                    //.append(right.input.time, left.input.time)
                    .toComparison()*/
                val leftTime = left.input.time
                val rightTime = right.input.time

                var comaparison = (rightTime - leftTime).toInt()
                if (comaparison < 0) {
                    comaparison = right.input.signal?.level.value - left.input.signal?.level.value
                }
                return comaparison
            }
            return 0
        }
    }

    class BssidComparator : Comparator<GenericItem> {
        override fun compare(left: GenericItem, right: GenericItem): Int {
            if (left is WifiItem && right is WifiItem) {
                return CompareToBuilder()
                    //.append(left.input.bssid, right.input.bssid)
                    .append(right.input.signal?.level, left.input.signal?.level)
                    .toComparison()
            }
            return 0
        }
    }

    /*class RankComparator : Comparator<GenericItem> {
        override fun compare(left: GenericItem, right: GenericItem): Int {
            if (left is CoinItem && right is CoinItem) {
                return left.item.rank - right.item.rank
            }
            return 0
        }
    }*/
}