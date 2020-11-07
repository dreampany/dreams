package com.dreampany.radio.ui.adapter

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.misc.exts.dimension
import com.dreampany.framework.ui.misc.ItemSpaceDecoration
import com.dreampany.radio.ui.model.StationItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.mikepenz.fastadapter.adapters.GenericFastItemAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook
import com.mikepenz.fastadapter.scroll.EndlessRecyclerOnScrollListener
import com.mikepenz.fastadapter.ui.items.ProgressItem
import timber.log.Timber
import com.dreampany.radio.R
import com.dreampany.radio.databinding.StationItemBinding
import com.mikepenz.fastadapter.binding.listeners.addClickListener

/**
 * Created by roman on 4/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class FastStationAdapter(
    val scrollListener: ((currentPage: Int) -> Unit)? = null,
    val clickListener: ((view: View, item: StationItem) -> Unit)? = null
) {

    private lateinit var scroller: EndlessRecyclerOnScrollListener
    private lateinit var fastAdapter: GenericFastItemAdapter
    private lateinit var itemAdapter: GenericItemAdapter
    private lateinit var footerAdapter: GenericItemAdapter

    val itemCount: Int
        get() = fastAdapter.adapterItems.size

    val isEmpty: Boolean get() = itemCount == 0

    fun initRecycler(
        state: Bundle?,
        recycler: RecyclerView
    ) {
        itemAdapter = ItemAdapter.items()
        itemAdapter.itemFilter.filterPredicate = { item: GenericItem, constraint: CharSequence? ->
            if (item is StationItem)
                item.input.name.toString().contains(constraint.toString(), ignoreCase = true)
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

        clickListener?.let { listener ->
            /*fastAdapter.onClickListener = { view, adapter, item, position ->
                if (item is CoinItem)
                    view?.let {
                        listener(it, item)
                    }
                false
            }*/
            fastAdapter.addClickListener<StationItemBinding, GenericItem>(
                { bind -> bind.root }, { bind -> arrayListOf(bind.favorite) }
            )
            { view, position, adapter, item ->
                if (item is StationItem) {
                    listener(view, item)
                }
            }
            /*fastAdapter.addClickListener<VideoItemBinding, GenericItem>(
                { bind -> bind.root },
                { bind -> arrayListOf(bind.root) }
            )
            { view, position, adapter, item ->
                if (item is VideoItem) {
                    listener(view, item)
                }
            }*/
        }

        fastAdapter.addEventHook(object : ClickEventHook<StationItem>() {
            /* override fun onBind(holder: RecyclerView.ViewHolder): View? {
                 if (holder is CoinItemBinding.)

                     return null
             }*/

            override fun onClick(
                view: View,
                position: Int,
                fastAdapter: FastAdapter<StationItem>,
                item: StationItem
            ) {
                Timber.v("View %s", view.toString())
            }

        })
    }

    fun destroy() {
    }

    fun clearAll() {
        fastAdapter.clear()
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

    fun updateItem(item: StationItem): Boolean {
        var position = fastAdapter.getAdapterPosition(item)
        position = fastAdapter.getGlobalPosition(position)
        if (position >= 0) {
            fastAdapter.set(position, item)
            return true
            //fastAdapter.notifyAdapterItemChanged(position)
        }
        return false
    }

    fun updateItems(items: List<StationItem>) {
        items.forEach {
            updateItem(it)
        }
    }

    fun addItem(item: StationItem) {
        val updated = updateItem(item)
        if (!updated)
            fastAdapter.add(item)
    }

    fun addItems(items: List<StationItem>) {
        fastAdapter.add(items)
    }

}