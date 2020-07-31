package com.dreampany.tools.ui.more.adapter

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.adapter.SpacingItemDecoration
import com.dreampany.framework.misc.exts.dimension
import com.dreampany.tools.R
import com.dreampany.tools.ui.more.model.MoreItem
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.mikepenz.fastadapter.adapters.GenericFastItemAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import timber.log.Timber

/**
 * Created by roman on 13/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class FastMoreAdapter(val clickListener : ((item: MoreItem) -> Unit)? = null) {

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
        fastAdapter = FastItemAdapter(itemAdapter)

        recycler.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = fastAdapter
            addItemDecoration(
                SpacingItemDecoration(
                    3,
                    context.dimension(R.dimen.recycler_spacing).toInt(),
                    false
                )
            )
        }
        fastAdapter.withSavedInstanceState(state)

        clickListener?.let {
            fastAdapter.onClickListener = { view, adapter, item, position ->
                Timber.v("View %s", view.toString())
                if (item is MoreItem) {
                    it (item)
                }
                false
            }
        }
    }

    fun saveInstanceState(outState: Bundle): Bundle {
        return fastAdapter.saveInstanceState(outState)
    }

    fun addItems(items: List<MoreItem>) {
        fastAdapter.add(items)
    }

}