package com.dreampany.radio.ui.page

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.misc.exts.addDecoration
import com.dreampany.radio.databinding.PageItemBinding
import com.dreampany.radio.ui.model.PageItem
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.mikepenz.fastadapter.adapters.GenericFastItemAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.binding.listeners.addClickListener
import com.mikepenz.fastadapter.drag.ItemTouchCallback
import com.mikepenz.fastadapter.drag.SimpleDragCallback
import com.mikepenz.fastadapter.utils.DragDropUtil

/**
 * Created by roman on 28/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class FastPageAdapter(
    val clickListener: ((view: View, item: PageItem) -> Unit)? = null
) : ItemTouchCallback {

    private lateinit var fastAdapter: GenericFastItemAdapter
    private lateinit var itemAdapter: GenericItemAdapter

    private lateinit var touchCallback: SimpleDragCallback
    private lateinit var touchHelper: ItemTouchHelper

    val itemCount: Int
        get() = fastAdapter.adapterItems.size

    val isEmpty: Boolean get() = itemCount == 0

    override fun itemTouchOnMove(oldPosition: Int, newPosition: Int): Boolean {
        DragDropUtil.onMove(itemAdapter, oldPosition, newPosition)
        return true
    }

    override fun itemTouchDropped(oldPosition: Int, newPosition: Int) {
        super.itemTouchDropped(oldPosition, newPosition)
    }

    override fun itemTouchStartDrag(viewHolder: RecyclerView.ViewHolder) {
        super.itemTouchStartDrag(viewHolder)
    }

    fun initRecycler(
        state: Bundle?,
        recycler: RecyclerView
    ) {
        //val fastScrollIndicatorAdapter = FastScrollIndicatorAdapter<SimpleItem>()
        itemAdapter = ItemAdapter.items()
        fastAdapter = FastItemAdapter(itemAdapter)
        /*   val selectExtension = fastAdapter.getSelectExtension() as SelectExtension<CategoryItem>
           selectExtension.apply {
               isSelectable = true
               multiSelect = true
               selectionListener = object : ISelectionListener<CategoryItem> {
                   override fun onSelectionChanged(item: CategoryItem, selected: Boolean) {
                      Timber.i( "SelectedCount: " + selectExtension.selections.size + " ItemsCount: " + selectExtension.selectedItems.size)
                      Timber.i( "Selected " + item.isSelected)

                   }
               }
           }*/

        touchCallback = SimpleDragCallback(this)
        touchHelper = ItemTouchHelper(touchCallback)
        touchHelper.attachToRecyclerView(recycler)

        recycler.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = fastAdapter
            addDecoration(4)
            /*addItemDecoration(
                SpacingItemDecoration(
                    3,
                    context.dimension(R.dimen.recycler_vertical_spacing).toInt(),
                    true
                )
            )*/
        }
        fastAdapter.withSavedInstanceState(state)

        clickListener?.let { listener ->
            fastAdapter.addClickListener<PageItemBinding, GenericItem>(
                { bind -> bind.root },
                { bind -> arrayListOf(bind.root) }
            )
            { view, position, adapter, item ->
                if (item is PageItem) {
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

    val selectionCount: Int
        get() = fastAdapter.adapterItems.filter { (it as PageItem).select }.size

    val selectedItems: List<PageItem>
        get() = fastAdapter.adapterItems.filter { (it as PageItem).select }.map { it as PageItem }

    val isSelected : Boolean
        get() = selectionCount > 0

    fun toggle(item: PageItem) {
        item.select = item.select.not()
        updateItem(item)
    }

    fun updateItem(item: PageItem): Boolean {
        var position = fastAdapter.getAdapterPosition(item)
        position = fastAdapter.getGlobalPosition(position)
        if (position >= 0) {
            fastAdapter.set(position, item)
            return true
            //fastAdapter.notifyAdapterItemChanged(position)
        }
        return false
    }

    fun updateItems(items: List<PageItem>) {
        items.forEach {
            updateItem(it)
        }
    }

    fun addItem(item: PageItem) {
        val updated = updateItem(item)
        if (!updated)
            fastAdapter.add(item)
    }

    fun addItems(items: List<PageItem>) {
        fastAdapter.add(items)
    }
}