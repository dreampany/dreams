package com.dreampany.framework.ui.adapter

import android.view.View
import com.dreampany.framework.ui.model.BaseItem
import com.dreampany.framework.util.DataUtil
import eu.davidea.flexibleadapter.databinding.BindingFlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Hawladar Roman on 5/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
open class SmartAdapter<T : BaseItem<*, *, *>>(listener: Any?) :
    BindingFlexibleAdapter<T>(listener) {

    interface OnUiItemClickListener<T, R> {
        fun onUiItemClick(view: View, item: T, action: R)
        fun onUiItemLongClick(view: View, item: T, action: R)
    }

    var uiItemClickListener: OnUiItemClickListener<Any, Any>? = null
        private set
    var clickListener: View.OnClickListener? = null
        private set
    var longClickListener: View.OnLongClickListener? = null
        private set

    init {
        if (listener is OnUiItemClickListener<*, *>) {
            uiItemClickListener = listener as OnUiItemClickListener<Any, Any>?
        }

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

    open fun setItems(items: List<T>, comparator: Comparator<IFlexible<*>>) {
        clear()
        addItems(items, comparator)
    }

    open fun addItems(items: List<T>): Boolean {
        for (item in items) {
            addItem(item)
        }
        return true
    }

    open fun addItems(items: List<T>, comparator: Comparator<IFlexible<*>>): Boolean {
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

    fun setSelection(item: T, selected: Boolean) {
        clearSelection()
        addSelection(item, selected)
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