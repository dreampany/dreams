package com.dreampany.news.ui.adapter

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.adapter.SpacingItemDecoration
import com.dreampany.framework.misc.exts.dimension
import com.dreampany.news.ui.model.ArticleItem
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.mikepenz.fastadapter.adapters.GenericFastItemAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.binding.listeners.addClickListener
import com.mikepenz.fastadapter.scroll.EndlessRecyclerOnScrollListener
import com.mikepenz.fastadapter.ui.items.ProgressItem
import com.dreampany.news.R
import com.dreampany.news.databinding.ArticleItemBinding
import com.mikepenz.fastadapter.dsl.genericFastAdapter

/**
 * Created by roman on 29/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class FastArticleAdapter(
    val scrollListener: ((currentPage: Int) -> Unit)? = null,
    val clickListener: ((view: View, item: ArticleItem) -> Unit)? = null
) {

    private lateinit var scroller: EndlessRecyclerOnScrollListener
    private lateinit var fastAdapter: GenericFastItemAdapter
    private lateinit var itemAdapter: GenericItemAdapter
    private lateinit var footerAdapter: GenericItemAdapter

    //private lateinit var capComparator: Comparator<GenericItem>
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
        //capComparator = CryptoComparator(currency, sort, order)
        //val list = ComparableItemListImpl(comparator = capComparator)
        itemAdapter = ItemAdapter.items()
        itemAdapter.itemFilter.filterPredicate = { item: GenericItem, constraint: CharSequence? ->
            if (item is ArticleItem)
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

            fastAdapter.addClickListener<ArticleItemBinding, GenericItem>(
                { bind -> bind.root }, { bind -> arrayListOf(bind.root) }
            )
            { view, position, adapter, item ->
                if (item is ArticleItem) {
                    listener(view, item)
                }
            }
            /*fastAdapter.addClickListener<CoinItemBinding, GenericItem>(
                { bind -> bind.root }, { bind -> arrayListOf(bind.layoutOptions.buttonFavorite) }
            )
            { view, position, adapter, item ->
                if (item is CoinItem) {
                    listener(view, item)
                }
            }

            fastAdapter.addClickListener<ArticleItemBinding, GenericItem>(
                { bind -> bind.root },
                { bind -> arrayListOf(bind.buttonFavorite) }
            )
            { view, position, adapter, item ->
                if (item is CoinItem) {
                    listener(view, item)
                }
            }*/

            fastAdapter.addClickListener<ArticleItemBinding, GenericItem>(
                { bind -> bind.root },
                { bind -> arrayListOf(bind.root) }
            )
            { view, position, adapter, item ->
                if (item is ArticleItem) {
                    listener(view, item)
                }
            }
        }
    }

    fun destroy() {
        fastAdapter.clear()
    }

    fun clearAll() {
        fastAdapter.clear()
    }

    fun notifyUi() {
        fastAdapter.notifyDataSetChanged()
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

    fun updateItem(item: ArticleItem): Boolean {
        var position = fastAdapter.getAdapterPosition(item)
        position = fastAdapter.getGlobalPosition(position)
        if (position >= 0) {
            fastAdapter.set(position, item)
            return true
            //fastAdapter.notifyAdapterItemChanged(position)
        }
        return false
    }

    fun updateItems(items: List<ArticleItem>) {
        items.forEach {
            updateItem(it)
        }
    }

    fun addItem(item: ArticleItem) {
        val updated = updateItem(item)
        if (!updated)
            fastAdapter.add(item)
    }

    fun addItems(items: List<ArticleItem>) {
        fastAdapter.add(items)
    }
}