package com.dreampany.framework.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.misc.extensions.bindInflater
import com.dreampany.framework.misc.extensions.dpToPx
import com.dreampany.framework.misc.extensions.screenWidthInPx
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by roman on 2020-02-20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseAdapter<T, VH : BaseAdapter.ViewHolder<T, VH>> : RecyclerView.Adapter<VH>() {

    protected val items: MutableList<T>

    init {
        items = Collections.synchronizedList(ArrayList())
    }

    protected abstract fun getViewType(item: T): Int

    protected abstract fun getLayoutId(viewType: Int): Int

    protected abstract fun createViewHolder(bind: ViewDataBinding, viewType: Int): VH

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position) ?: return 0
        return getViewType(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutId = getLayoutId(viewType)
        return createViewHolder(layoutId.bindInflater(parent), viewType)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        Timber.v("ViewHolder Binding %s", holder.toString())
        getItem(position)?.run {
            holder.bindView(holder, this, position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    open fun isEmpty(): Boolean {
        return itemCount == 0
    }

    fun isFirst(item: T): Boolean {
        return isFirst(getPosition(item))
    }

    fun isLast(item: T): Boolean {
        return isLast(getPosition(item))
    }

    fun isMiddle(item: T): Boolean {
        return isMiddle(getPosition(item))
    }

    fun isFirst(position: Int): Boolean {
        return position == 0
    }

    fun isLast(position: Int): Boolean {
        return position == itemCount - 1
    }

    fun isMiddle(position: Int): Boolean {
        return position > 0 && position < itemCount - 1
    }

    open fun getItem(position: Int): T? {
        return if (!isValidPosition(position)) {
            null
        } else items[position]
    }

    open fun getPosition(item: T): Int {
        return items.indexOf(item)
    }

    open fun addAll(items: List<T>) {
        for (room in items) {
            add(room)
        }
    }

    open fun addAll(
        items: List<T>,
        comparator: Comparator<T>?
    ) {
        for (room in items) {
            add(room, comparator)
        }
    }

    open fun add(item: T) {
        val position = getPosition(item)
        if (position == -1) {
            items.add(item)
            notifyItemInserted(itemCount - 1)
        } else {
            items[position] = item
            notifyItemChanged(position)
        }
    }

    open fun add(item: T, comparator: Comparator<T>?) {
        var position = getPosition(item)
        if (position == -1) {
            position = calculatePositionFor(item, comparator)
            Timber.v("Calculated Position %d", position)
            performInsert(position, listOf(item), true)
            /*items.add(item);
            notifyItemInserted(getItemCount() - 1);*/
        } else {
            items[position] = item
            notifyItemChanged(position)
        }
    }

    open fun add(position: Int, item: T) {
        items.add(position, item)
        notifyItemInserted(position)
    }

    open fun removeAt(position: Int): T {
        val item = items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, items.size)
        return item
    }

    open fun clearAll() {
        items.clear()
        notifyDataSetChanged()
    }

    open fun notifyItemChanged(item: T) {
        val position = getPosition(item)
        notifyItemChanged(position)
    }

    protected open fun calculatePositionFor(item: T, comparator: Comparator<T>?): Int {
        if (comparator == null) return 0
        val sortedList: MutableList<T> = ArrayList(items)
        if (!sortedList.contains(item)) sortedList.add(item)
        Collections.sort(sortedList, comparator)
        return Math.max(0, sortedList.indexOf(item))
    }

    private fun performInsert(
        position: Int,
        items: List<T>,
        notify: Boolean
    ) {
        var position = position
        val itemCount = itemCount
        if (position < itemCount) {
            this.items.addAll(position, items)
        } else {
            this.items.addAll(items)
            position = itemCount
        }
        // Notify range addition
        if (notify) {
            Timber.v("addItems on position=%s itemCount=%s", position, items.size)
            notifyItemRangeInserted(position, items.size)
        }
    }

    private fun isValidPosition(position: Int): Boolean {
        return position >= 0 && position < itemCount
    }

    abstract class ViewHolder<T, VH : RecyclerView.ViewHolder> protected constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        protected val context: Context get() = itemView.context

        abstract fun bindView(holder: VH, item: T, position: Int)

        open fun getSpanHeight(spanCount: Int, itemOffset: Int): Int {
            return (context.screenWidthInPx() / spanCount) - (itemOffset.dpToPx() * spanCount)
        }

        protected fun getString(@StringRes resId: Int): String {
            return context.getString(resId)
        }

        protected fun getString(@StringRes resId: Int, vararg args: Any?): String {
            return context.getString(resId, *args)
        }
    }
}