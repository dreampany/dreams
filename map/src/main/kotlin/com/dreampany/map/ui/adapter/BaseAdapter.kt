package com.dreampany.map.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import java.util.*

/**
 * Created by roman on 2019-12-04
 * Copyright (c) 2019 bjit. All rights reserved.
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
    protected abstract fun createViewHolder(view: View, viewType: Int): VH

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position) ?: return 0
        return getViewType(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutId = getLayoutId(viewType)
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(layoutId, parent, false)
        return createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        Timber.v("ViewHolder Binding %s", holder.toString())
        val item: T? = getItem(position)
        item?.run {
            holder.bindView(holder, this)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun isEmpty(): Boolean {
        return itemCount == 0
    }

    fun getItem(position: Int): T? {
        return if (!isValidPosition(position)) { null
        } else items[position]
    }

    fun getPosition(item: T): Int {
        return items.indexOf(item)
    }

    fun addAll(items: List<T>) {
        for (room in items) {
            add(room)
        }
    }

    fun addAll(
        items: List<T>,
        comparator: Comparator<T>?
    ) {
        for (room in items) {
            add(room, comparator)
        }
    }

    fun add(item: T) {
        val position = getPosition(item)
        if (position == -1) {
            items.add(item)
            notifyItemInserted(itemCount - 1)
        } else {
            items[position] = item
            notifyItemChanged(position)
        }
    }

    fun add(item: T, comparator: Comparator<T>?) {
        var position = getPosition(item)
        if (position == -1) {
            position = calculatePositionFor(item, comparator)
            Timber.v("Calculated Position %d", position)
            performInsert(position, listOf(item), true)
            /*            items.add(item);
            notifyItemInserted(getItemCount() - 1);*/
        } else {
            items[position] = item
            notifyItemChanged(position)
        }
    }

    fun add(position: Int, item: T) {
        items.add(position, item)
        notifyItemInserted(position)
    }

    fun removeAt(position: Int): T {
        val item = items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, items.size)
        return item
    }

    fun clearAll() {
        items.clear()
        notifyDataSetChanged()
    }

    fun notifyItemChanged(item: T) {
        val position = getPosition(item)
        notifyItemChanged(position)
    }

    protected fun calculatePositionFor(item: T, comparator: Comparator<T>?): Int {
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

    abstract class ViewHolder<T, VH : ViewHolder<T, VH>>(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        abstract fun bindView(holder: VH, item: T)
        val context: Context get() = itemView.getContext()

        fun getString(@StringRes resId: Int): String {
            return context.getString(resId)
        }

        fun getString(@StringRes resId: Int, vararg args: Any?): String {
            return context.getString(resId, *args)
        }
    }
}