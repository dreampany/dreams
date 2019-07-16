package com.dreampany.frame.ui.adapter

import android.view.View
import com.dreampany.frame.ui.model.BaseItemKt
import com.dreampany.frame.util.DataUtil
import eu.davidea.flexibleadapter.databinding.BindingFlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.List

/**
 * Created by Roman-372 on 7/11/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
open class SmartAdapterKt<T : BaseItemKt<*, *, *>>(listener: Any?) :
    BindingFlexibleAdapter<T>(listener) {

    var clickListener: View.OnClickListener? = null
        private set
    var longClickListener: View.OnLongClickListener? = null
        private set

    init {
        if (listener is View.OnClickListener) {
            clickListener = listener
        }
        if (listener is View.OnLongClickListener) {
            longClickListener = listener
        }
    }

    interface Callback<T> {
        val empty: Boolean
        val items: List<T>?
        val visibleItems: List<T>?
        val visibleItem: T?
    }

    override fun addItem(item: T): Boolean {
        if (contains(item)) {
            updateItem(item)
            return true
        } else {
            return super.addItem(item)
        }
    }

    override fun addItem(position: Int, item: T): Boolean {
        if (contains(item)) {
            updateItem(item)
            return true
        } else {
            return super.addItem(position, item)
        }
    }

    fun addItem(item: T, comparator: Comparator<IFlexible<*>>): Boolean {
        if (contains(item)) {
            updateItem(item)
            return true
        } else {
            try {
                return super.addItem(calculatePositionFor(item, comparator), item)
            } catch (ignored: Exception) {
                return false;
            }
        }
    }

    fun setItems(items: List<T>) {
        for (item in items) {
            addItem(itemCount, item)
        }
    }

    fun addItems(items: List<T>): Boolean {
        return addItems(0, items)
    }

    fun addItems(items: List<T>, comparator: Comparator<IFlexible<*>>): Boolean {
        if (items.isEmpty()) {
            return false
        }
        for (item in items) {
            addItem(item, comparator)
        }
        return true
    }

    fun removeItem(item: T) {
        super.removeItem(getPosition(item))
    }

    fun removeItem(items: List<T>) {
        for (item in items) {
            removeItem(item)
        }
    }

    override fun toggleSelection(position: Int) {
        super.toggleSelection(position)
        notifyItemChanged(position)
    }

    fun getPosition(item: T): Int {
        return getGlobalPositionOf(item)
    }

    fun isAnySelected(): Boolean {
        return selectedItemCount > 0
    }

    fun addSelection(item: T, selected: Boolean) {
        val position = getGlobalPositionOf(item)
        if (position >= 0) {
            if (selected) {
                addSelection(position)
            } else {
                removeSelection(position)
            }
            notifyItemChanged(position)
        }
    }

    fun selectAll() {
        super.selectAll()
        notifyDataSetChanged()
    }

    override fun clearSelection() {
        super.clearSelection()
        notifyDataSetChanged()
    }

    fun isSelected(item: T): Boolean {
        return isSelected(getGlobalPositionOf(item))
    }

    fun getSelectedItems(): List<T>? {
        if (selectedItemCount > 0) {
            val positions = selectedPositions
            val items = ArrayList<T>(positions.size)
            for (position in positions) {
                val item: T? = getItem(position)
                if (item != null) {
                    items.add(item)
                }
            }
            return items
        }
        return null
    }

    fun updateSilently(item: T) {
        if (contains(item)) {
            updateItem(item)
        }
    }

    fun updateSilently(items: List<T>) {
        for (item in items) {
            updateItem(item)
        }
    }

    fun replace(items: List<T>) {
        //clear()
    }

    @Synchronized
    fun getVisibleItems(): List<T>? {
        if (isEmpty) {
            return null
        }
        val layout = flexibleLayoutManager
        val first = layout.findFirstCompletelyVisibleItemPosition()
        val last = layout.findLastCompletelyVisibleItemPosition()
        val items = ArrayList<T>()
        for (pos in first..last) {
            getItem(pos)?.let { items.add(it) }
        }
        return items
    }

    @Synchronized
    fun getVisibleItem(): T? {
        val items = getVisibleItems()
        return DataUtil.getRandomItem(items);
    }
}