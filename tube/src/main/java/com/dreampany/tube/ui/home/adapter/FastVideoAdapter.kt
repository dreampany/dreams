package com.dreampany.tube.ui.home.adapter

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.util.ViewInfo
import com.dreampany.adapter.SpacingItemDecoration
import com.dreampany.framework.data.enums.Order
import com.dreampany.framework.misc.exts.dimension
import com.dreampany.tube.R
import com.dreampany.tube.databinding.VideoItemBinding
import com.dreampany.tube.ui.home.model.VideoItem
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.mikepenz.fastadapter.adapters.GenericFastItemAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.binding.listeners.addClickListener
import com.mikepenz.fastadapter.scroll.EndlessRecyclerOnScrollListener
import com.mikepenz.fastadapter.ui.items.ProgressItem
import com.mikepenz.fastadapter.utils.ComparableItemListImpl

/**
 * Created by roman on 2/7/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class FastVideoAdapter(
    val scrollListener: ((currentPage: Int) -> Unit)? = null,
    val clickListener: ((view: View, item: VideoItem) -> Unit)? = null
) {

    private lateinit var scroller: EndlessRecyclerOnScrollListener
    private lateinit var fastAdapter: GenericFastItemAdapter
    private lateinit var itemAdapter: GenericItemAdapter
    private lateinit var footerAdapter: GenericItemAdapter

    private val viewComparator: Comparator<GenericItem>

    init {
        viewComparator = ViewComparator()
    }

    val itemCount: Int
        get() = fastAdapter.itemCount

    val isEmpty: Boolean get() = itemCount == 0

    fun initRecycler(
        state: Bundle?,
        recycler: RecyclerView
    ) {
        val list = ComparableItemListImpl(comparator = viewComparator)
        itemAdapter = ItemAdapter(list)
        itemAdapter.itemFilter.filterPredicate = { item: GenericItem, constraint: CharSequence? ->
            if (item is VideoItem)
                item.input.title.toString().contains(constraint.toString(), ignoreCase = true)
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
            fastAdapter.addClickListener<VideoItemBinding, GenericItem>(
                { bind -> bind.root }, { bind -> arrayListOf(bind.root) }
            )
            { view, position, adapter, item ->
                if (item is VideoItem) {
                    listener(view, item)
                }
            }

            fastAdapter.addClickListener<VideoItemBinding, GenericItem>(
                { bind -> bind.root },
                { bind -> arrayListOf(bind.root) }
            )
            { view, position, adapter, item ->
                if (item is VideoItem) {
                    listener(view, item)
                }
            }
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

    fun updateItem(item: VideoItem): Boolean {
        var position = fastAdapter.getAdapterPosition(item)
        position = fastAdapter.getGlobalPosition(position)
        if (position >= 0) {
            fastAdapter.set(position, item)
            return true
            //fastAdapter.notifyAdapterItemChanged(position)
        }
        return false
    }

    fun updateItems(items: List<VideoItem>) {
        items.forEach {
            updateItem(it)
        }
    }

    fun addItem(item: VideoItem) {
        val updated = updateItem(item)
        if (!updated)
            fastAdapter.add(item)
    }

    fun addItems(items: List<VideoItem>) {
        fastAdapter.add(items)
    }

    class ViewComparator(
    ) : Comparator<GenericItem> {
        override fun compare(left: GenericItem, right: GenericItem): Int {
            if (left is VideoItem && right is VideoItem) {
                return (right.input.viewCount - left.input.viewCount).toInt()
            }
            return 0
        }
    }
}