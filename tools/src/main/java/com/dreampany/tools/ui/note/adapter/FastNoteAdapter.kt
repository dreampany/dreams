package com.dreampany.tools.ui.note.adapter

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.adapter.SpacingItemDecoration
import com.dreampany.framework.misc.extension.dimension
import com.dreampany.tools.R
import com.dreampany.tools.databinding.CoinInfoItemBinding
import com.dreampany.tools.databinding.CoinItemBinding
import com.dreampany.tools.databinding.NoteItemBinding
import com.dreampany.tools.ui.note.model.NoteItem
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.mikepenz.fastadapter.adapters.GenericFastItemAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.binding.listeners.addClickListener
import com.mikepenz.fastadapter.scroll.EndlessRecyclerOnScrollListener
import com.mikepenz.fastadapter.ui.items.ProgressItem

/**
 * Created by roman on 13/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class FastNoteAdapter(
    val scrollListener: ((currentPage: Int) -> Unit)? = null,
    val clickListener: ((view: View, item: NoteItem) -> Unit)? = null
) {

    private lateinit var scroller: EndlessRecyclerOnScrollListener
    private lateinit var fastAdapter: GenericFastItemAdapter
    private lateinit var itemAdapter: GenericItemAdapter
    private lateinit var footerAdapter: GenericItemAdapter

    val itemCount: Int
        get() = fastAdapter.itemCount

    val isEmpty: Boolean get() = itemCount == 0

    fun initRecycler(
        state: Bundle?,
        recycler: RecyclerView
    ) {
        itemAdapter = ItemAdapter.items()
        itemAdapter.itemFilter.filterPredicate = { item: GenericItem, constraint: CharSequence? ->
            if (item is NoteItem)
                item.input.title.contains(constraint.toString(), ignoreCase = true)
            else
                false
        }
        footerAdapter = ItemAdapter.items()
        fastAdapter = FastItemAdapter(itemAdapter)
        fastAdapter.addAdapter(1, footerAdapter)

        recycler.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = fastAdapter
            addItemDecoration(
                SpacingItemDecoration(
                    2,
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
            fastAdapter.addClickListener<NoteItemBinding, GenericItem>(
                { bind -> bind.root },
                { bind ->
                    arrayListOf(
                        bind.buttonEdit
                    )
                }
            )
            { view, position, adapter, item ->
                if (item is NoteItem) {
                    listener(view, item)
                }
            }

           /* fastAdapter.addClickListener<CoinInfoItemBinding, GenericItem>(
                { bind -> bind.root },
                { bind -> arrayListOf(bind.bu) }
            )
            { view, position, adapter, item ->
                if (item is NoteItem) {
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

    fun updateItem(item: NoteItem) {
        var position = fastAdapter.getAdapterPosition(item)
        position = fastAdapter.getGlobalPosition(position)
        if (position >= 0) {
            fastAdapter.set(position, item)
            //fastAdapter.notifyAdapterItemChanged(position)
        } else {
            fastAdapter.add(item)
        }
    }

    fun updateItems(items: List<NoteItem>) {
        items.forEach {
            updateItem(it)
        }
    }

    fun addItems(items: List<NoteItem>) {
        fastAdapter.add(items)
    }
}